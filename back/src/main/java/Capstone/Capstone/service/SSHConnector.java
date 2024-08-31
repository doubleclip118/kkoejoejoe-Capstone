package Capstone.Capstone.service;

import com.jcraft.jsch.*;
import java.net.UnknownHostException;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Properties;
import java.util.Set;

@Service
public class SSHConnector {
    private static final Logger logger = LoggerFactory.getLogger(SSHConnector.class);

    static {
        JSch.setLogger(new com.jcraft.jsch.Logger() {
            @Override
            public boolean isEnabled(int level) { return true; }
            @Override
            public void log(int level, String message) {
                logger.info("JSch Log: {}", message);
            }
        });
    }

    public Session connectToEC2(String privateKeyContent, String ec2IpAddress) throws JSchException, IOException {
        Path tempKeyFile = null;
        try {
            String formattedKey = formatPrivateKey(privateKeyContent);
            tempKeyFile = createTempKeyFile(formattedKey);

            JSch jsch = new JSch();
            jsch.addIdentity(tempKeyFile.toString());

            String user = "ubuntu";
            int port = 22;

            logger.info("Connecting to {} with user {}", ec2IpAddress, user);

            testNetworkConnection(ec2IpAddress);

            Session session = jsch.getSession(user, ec2IpAddress, port);
            configureSession(session);

            logger.info("Attempting to connect...");
            session.connect(30000); // 30 seconds timeout
            logger.info("Connected successfully");

            return session;
        } finally {
            if (tempKeyFile != null) {
                Files.deleteIfExists(tempKeyFile);
            }
        }
    }

    private String formatPrivateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Private key content cannot be null or empty");
        }

        key = key.trim();

        // 시작과 끝 구분자 제거
        key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replaceAll("\\s+", "");

        // Base64 디코딩 및 인코딩을 통해 유효성 검사
        try {
            byte[] decodedKey = Base64.getDecoder().decode(key);
            key = Base64.getEncoder().encodeToString(decodedKey);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 encoding in private key", e);
        }

        // 64자마다 줄바꿈 추가
        StringBuilder formattedKey = new StringBuilder("-----BEGIN RSA PRIVATE KEY-----\n");
        for (int i = 0; i < key.length(); i += 64) {
            formattedKey.append(key.substring(i, Math.min(i + 64, key.length()))).append("\n");
        }
        formattedKey.append("-----END RSA PRIVATE KEY-----");

        return formattedKey.toString();
    }

    private Path createTempKeyFile(String key) throws IOException {
        Path tempDir = Files.createTempDirectory("ssh-temp-dir");
        Path tempKeyFile = tempDir.resolve("temp.pem");
        Files.write(tempKeyFile, key.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
        Files.setPosixFilePermissions(tempKeyFile, perms);
        return tempKeyFile;
    }

    private void testNetworkConnection(String ec2IpAddress) throws IOException {
        try {
            InetAddress address = InetAddress.getByName(ec2IpAddress);
            logger.info("Attempting to reach host: {}", ec2IpAddress);

            if (address.isReachable(10000)) {  // 타임아웃을 10초로 늘림
                logger.info("Host is reachable");
            } else {
                logger.error("Host is not reachable: {}", ec2IpAddress);
                throw new IOException("Host is not reachable: " + ec2IpAddress);
            }
        } catch (UnknownHostException e) {
            logger.error("Unknown host: {}", ec2IpAddress, e);
            throw new IOException("Unknown host: " + ec2IpAddress, e);
        } catch (IOException e) {
            logger.error("IO error occurred while testing network connection to {}", ec2IpAddress, e);
            throw e;
        }
    }

    private void configureSession(Session session) {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
    }

    public void disconnectFromEC2(Session session) {
        if (session != null && session.isConnected()) {
            logger.info("Disconnecting from EC2...");
            session.disconnect();
            logger.info("Disconnected successfully");
        }
    }

    public String executeCommand(Session session, String command) throws JSchException, IOException {
        logger.info("Executing command: {}", command);
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            channel.setErrStream(errorStream);

            channel.connect();

            while (channel.isConnected()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Command execution interrupted", e);
                }
            }

            String response = new String(responseStream.toByteArray());
            String error = new String(errorStream.toByteArray());

            if (!error.isEmpty()) {
                logger.error("Error executing command: {}", error);
                throw new JSchException("Error executing command: " + error);
            }

            logger.info("Command executed successfully");
            return response;
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    public void sendPemKeyViaSftp(Session session, String privateKeyContent) throws JSchException, SftpException, IOException {
        Path tempKeyFile = null;
        ChannelSftp channelSftp = null;
        String remoteFileName = "temp_key.pem";
        String remotePemPath = "/home/ubuntu/" + remoteFileName;
        try {
            String formattedKey = formatPrivateKey(privateKeyContent);
            tempKeyFile = createTempKeyFile(formattedKey);

            logger.info("Opening SFTP channel...");
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            logger.info("SFTP channel opened.");

            logger.info("Sending PEM key file to {}", remotePemPath);
            channelSftp.put(tempKeyFile.toString(), remotePemPath);
            logger.info("PEM key file sent successfully.");

            // Set appropriate permissions for the PEM key file
            channelSftp.chmod(0600, remotePemPath);
            logger.info("PEM key file permissions set to 600.");

        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
                logger.info("SFTP channel closed.");
            }
            if (tempKeyFile != null) {
                Files.deleteIfExists(tempKeyFile);
                logger.info("Temporary key file deleted.");
            }
        }
    }
}
