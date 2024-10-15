from flask import Flask, request, jsonify
import paramiko
import io
import os
import tempfile
import logging
from paramiko.ssh_exception import SSHException
from flask_cors import CORS  # 추가

app = Flask(__name__)
CORS(app)  # CORS 설정을 추가하여 모든 도메인에서 접근 허용

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Helper functions
def format_private_key(key):
    key = key.strip()
    if not key.startswith("-----BEGIN RSA PRIVATE KEY-----"):
        key = "-----BEGIN RSA PRIVATE KEY-----\n" + key
    if not key.endswith("-----END RSA PRIVATE KEY-----"):
        key = key + "\n-----END RSA PRIVATE KEY-----"

    lines = key.split('\n')
    formatted_key = lines[0] + '\n'
    for line in lines[1:-1]:
        formatted_key += '\n'.join([line[i:i + 64] for i in range(0, len(line), 64)]) + '\n'
    formatted_key += lines[-1]

    return formatted_key

def create_temp_key_file(key_content):
    fd, path = tempfile.mkstemp()
    os.write(fd, key_content.encode())
    os.close(fd)
    os.chmod(path, 0o600)
    return path

def test_network_connection(host):
    import socket
    try:
        socket.create_connection((host, 22), timeout=5)
        logger.info(f"Host {host} is reachable")
        return True
    except Exception as e:
        logger.error(f"Host {host} is not reachable: {str(e)}")
        return False

def connect_to_ec2(private_key_content, ec2_ip_address, csp):
    try:
        # 비공개 키를 로드합니다.
        key = paramiko.RSAKey.from_private_key(io.StringIO(private_key_content))
    except paramiko.SSHException as e:
        logger.error(f"Private key is invalid: {str(e)}")
        raise

    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    if not test_network_connection(ec2_ip_address):
        raise Exception(f"Host is not reachable: {ec2_ip_address}")

    # CSP에 따라 사용자 이름을 설정합니다.
    if csp.lower() == 'aws':
        username = 'cb-user'
    elif csp.lower() == 'azure':
        username = 'cb-user'
    else:
        raise ValueError(f"Unsupported CSP: {csp}")

    try:
        logger.info(f"username {username}")
        client.connect(hostname=ec2_ip_address, username=username, pkey=key, timeout=300)
        logger.info(f"Connected successfully to {ec2_ip_address}")
        return client
    except paramiko.AuthenticationException:
        logger.error("Authentication failed, please verify your credentials.")
        raise
    except paramiko.SSHException as sshException:
        logger.error(f"Unable to establish SSH connection: {str(sshException)}")
        raise
    except Exception as e:
        logger.error(f"Failed to connect to EC2: {str(e)}")
        raise


def execute_command(client, command):
    logger.info(f"Executing command: {command}")
    stdin, stdout, stderr = client.exec_command(command)
    output = stdout.read().decode()
    error = stderr.read().decode()

    if error and "% Total    % Received % Xferd" not in error:
        logger.error(f"Error executing command: {error}")
        raise Exception(f"Error executing command: {error}")

    logger.info(f"Command executed successfully. Response: {output}")
    return output

def send_pem_key_via_sftp(client, private_key_content, remote_path):
    formatted_key = format_private_key(private_key_content)
    temp_key_path = create_temp_key_file(formatted_key)

    try:
        sftp = client.open_sftp()
        logger.info(f"Sending PEM key file to {remote_path}")
        sftp.put(temp_key_path, remote_path)
        logger.info("PEM key file sent successfully.")

        sftp.chmod(remote_path, 0o600)
        logger.info("PEM key file permissions set to 600.")
    finally:
        if 'sftp' in locals():
            sftp.close()
        os.remove(temp_key_path)
        logger.info("Temporary key file deleted.")

@app.route('/api/network', methods=['POST'])
def setup_network():
    data = request.json
    ca_ip = data['caIP']
    org_ip = data['orgIP']
    ca_secret_key = data['caSecretKey']
    org_secret_key = data['orgSecretKey']
    ca_csp = data['caCSP']
    org_csp = data['orgCSP']

    # CSP에 따라 pem 경로 설정
    if org_csp.lower() == 'aws':
        org_remote_pem_path = "/home/cb-user/temp.pem"
    elif org_csp.lower() == 'azure':
        org_remote_pem_path = "/home/cb-user/temp.pem"

    try:
        # Step 1: Connect to CA VM and run CA setup script
        ca_client = connect_to_ec2(ca_secret_key, ca_ip,ca_csp)
        execute_command(ca_client, "curl -L -o $PWD/startup-ca.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-ca.sh")
        execute_command(ca_client, "chmod +x startup-ca.sh")
        execute_command(ca_client, f"./startup-ca.sh {ca_ip}")
        ca_client.close()

        # Step 2: Connect to ORG1 VM and transfer the PEM file
        org_client = connect_to_ec2(org_secret_key, org_ip,org_csp)
        send_pem_key_via_sftp(org_client, ca_secret_key, org_remote_pem_path)

        # Step 3: Run ORG1 setup script
        execute_command(org_client, f"chmod 400 {org_remote_pem_path}")
        execute_command(org_client, "curl -L -o $PWD/startup-org1.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-org1.sh")
        execute_command(org_client, "chmod +x startup-org1.sh")
        execute_command(org_client, f"./startup-org1.sh {ca_ip} > log.txt 2>&1")
        org_client.close()

        return jsonify({'message': 'CA and ORG1 setup successfully completed'})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
