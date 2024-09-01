from flask import Flask, request, jsonify
import paramiko
import io
import os
import tempfile
import logging
from paramiko.ssh_exception import SSHException

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def format_private_key(key):
    key = key.strip()
    if not key.startswith("-----BEGIN RSA PRIVATE KEY-----"):
        key = "-----BEGIN RSA PRIVATE KEY-----\n" + key
    if not key.endswith("-----END RSA PRIVATE KEY-----"):
        key = key + "\n-----END RSA PRIVATE KEY-----"
    
    lines = key.split('\n')
    formatted_key = lines[0] + '\n'
    for line in lines[1:-1]:
        formatted_key += '\n'.join([line[i:i+64] for i in range(0, len(line), 64)]) + '\n'
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
    except:
        logger.error(f"Host {host} is not reachable")
        return False

def connect_to_ec2(private_key_content, ec2_ip_address):
    key = paramiko.RSAKey.from_private_key(io.StringIO(private_key_content))
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    
    if not test_network_connection(ec2_ip_address):
        raise Exception(f"Host is not reachable: {ec2_ip_address}")
    
    try:
        client.connect(hostname=ec2_ip_address, username="ubuntu", pkey=key, timeout=300)
        logger.info(f"Connected successfully to {ec2_ip_address}")
        return client
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

def send_pem_key_via_sftp(client, private_key_content):
    formatted_key = format_private_key(private_key_content)
    temp_key_path = create_temp_key_file(formatted_key)
    
    try:
        sftp = client.open_sftp()
        remote_file_name = "temp_key.pem"
        remote_pem_path = f"/home/ubuntu/{remote_file_name}"
        
        logger.info(f"Sending PEM key file to {remote_pem_path}")
        sftp.put(temp_key_path, remote_pem_path)
        logger.info("PEM key file sent successfully.")
        
        sftp.chmod(remote_pem_path, 0o600)
        logger.info("PEM key file permissions set to 600.")
    finally:
        if 'sftp' in locals():
            sftp.close()
        os.remove(temp_key_path)
        logger.info("Temporary key file deleted.")

@app.route('/execute', methods=['POST'])
def execute():
    data = request.json
    private_key_content = data['private_key']
    ec2_ip_address = data['ec2_ip_address']
    command = data['command']
    
    try:
        formatted_key = format_private_key(private_key_content)
        client = connect_to_ec2(formatted_key, ec2_ip_address)
        result = execute_command(client, command)
        client.close()
        return jsonify({'result': result})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/send_pem_key', methods=['POST'])
def send_pem_key():
    data = request.json
    private_key_content = data['private_key']
    ec2_ip_address = data['ec2_ip_address']
    key_to_send = data['key_to_send']
    
    try:
        formatted_key = format_private_key(private_key_content)
        client = connect_to_ec2(formatted_key, ec2_ip_address)
        send_pem_key_via_sftp(client, key_to_send)
        client.close()
        return jsonify({'message': 'PEM key sent successfully'})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)