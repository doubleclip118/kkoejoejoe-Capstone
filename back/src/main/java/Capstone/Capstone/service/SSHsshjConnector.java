package Capstone.Capstone.service;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.sftp.SFTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class SSHsshjConnector {
    private static final Logger logger = LoggerFactory.getLogger(SSHConnector.class);

    public SSHClient connectToEC2(String privateKeyContent, String ec2IpAddress) throws IOException {
        SSHClient ssh = new SSHClient();
        try {
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(ec2IpAddress);

            KeyProvider keyProvider = ssh.loadKeys(privateKeyContent, null, null);
            ssh.authPublickey("ubuntu", keyProvider);

            logger.info("Connected successfully to {}", ec2IpAddress);
            return ssh;
        } catch (Exception e) {
            logger.error("Failed to connect to EC2: {}", e.getMessage(), e);
            ssh.close();
            throw new IOException("Failed to connect to EC2", e);
        }
    }

    public String executeCommand(SSHClient ssh, String command) throws IOException {
        try (Session session = ssh.startSession()) {
            Command cmd = session.exec(command);
            String output = new String(cmd.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String error = new String(cmd.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            cmd.join();

            // curl 진행 상황 출력 패턴
            String curlProgressPattern = "\\s*%\\s+Total\\s+%\\s+Received\\s+%\\s+Xferd\\s+Average\\s+Speed\\s+Time\\s+Time\\s+Time\\s+Current.*\\n?";

            // curl 진행 상황 출력을 제거
            error = error.replaceAll(curlProgressPattern, "");

            if (!error.trim().isEmpty()) {
                logger.error("Error executing command: {}", error);
                throw new IOException("Error executing command: " + error);
            }

            // curl 진행 상황 출력을 output에서도 제거
            output = output.replaceAll(curlProgressPattern, "");

            logger.info("Command executed successfully. Output: {}", output);
            return output;
        }
    }

    public void sendPemKeyViaSftp(SSHClient ssh, String privateKeyContent) throws IOException {
        Path tempFile = null;
        try {
            // 임시 파일 생성
            tempFile = Files.createTempFile("temp_key", ".pem");
            Files.write(tempFile, privateKeyContent.getBytes(StandardCharsets.UTF_8));

            try (SFTPClient sftp = ssh.newSFTPClient()) {
                String remoteFileName = "temp_key.pem";
                String remotePemPath = "/home/ubuntu/" + remoteFileName;

                logger.info("Sending PEM key file to {}", remotePemPath);

                // 로컬 임시 파일을 원격 서버로 전송
                sftp.put(tempFile.toString(), remotePemPath);

                // 파일 권한 설정
                sftp.chmod(remotePemPath, 0600);
                logger.info("PEM key file sent successfully and permissions set to 600.");
            }
        } finally {
            // 임시 파일 삭제
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
        }
    }

    public void disconnectFromEC2(SSHClient ssh) {
        if (ssh != null && ssh.isConnected()) {
            try {
                ssh.disconnect();
                logger.info("Disconnected successfully");
            } catch (IOException e) {
                logger.error("Error disconnecting from EC2", e);
            }
        }
    }
}
