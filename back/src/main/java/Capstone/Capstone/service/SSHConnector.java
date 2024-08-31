package Capstone.Capstone.service;

import com.jcraft.jsch.*;
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

    public String executeCommandWithNewSession(String privateKeyContent, String ec2IpAddress, String command) throws JSchException, IOException {
        Session session = null;
        try {
            session = connectToEC2(privateKeyContent, ec2IpAddress);
            return executeCommand(session, command);
        } finally {
            disconnectFromEC2(session);
        }
    }

    public void sendPemKeyViaSftpWithNewSession(String privateKeyContent, String ec2IpAddress, String keyToSend) throws JSchException, SftpException, IOException {
        Session session = null;
        try {
            session = connectToEC2(privateKeyContent, ec2IpAddress);
            sendPemKeyViaSftp(session, keyToSend);
        } finally {
            disconnectFromEC2(session);
        }
    }

    private Session connectToEC2(String privateKeyContent, String ec2IpAddress) throws JSchException, IOException {
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
            session.connect(300000); // 300 seconds timeout
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

        // 기존 헤더와 푸터가 존재하는지 확인
        if (!key.startsWith("-----BEGIN RSA PRIVATE KEY-----")) {
            key = "-----BEGIN RSA PRIVATE KEY-----\n" + key;
        }
        if (!key.endsWith("-----END RSA PRIVATE KEY-----")) {
            key = key + "\n-----END RSA PRIVATE KEY-----";
        }

        // 헤더와 푸터 부분을 제외하고 본문만 64자씩 줄바꿈
        String base64Content = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replaceAll("\\s", "");  // 모든 공백 제거

        StringBuilder formattedKey = new StringBuilder("-----BEGIN RSA PRIVATE KEY-----\n");
        for (int i = 0; i < base64Content.length(); i += 64) {
            formattedKey.append(base64Content, i, Math.min(i + 64, base64Content.length())).append("\n");
        }
        formattedKey.append("-----END RSA PRIVATE KEY-----\n");

        logger.info(formattedKey.toString().trim());
        return formattedKey.toString().trim();
    }



    private Path createTempKeyFile(String key) throws IOException {
        Path tempDir = Files.createTempDirectory("ssh-temp-dir");
        Path tempKeyFile = tempDir.resolve("temp.pem");
        Files.write(tempKeyFile, key.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwx------");
        Files.setPosixFilePermissions(tempKeyFile, perms);
        return tempKeyFile;
    }

    private void testNetworkConnection(String ec2IpAddress) throws IOException {
        InetAddress address = InetAddress.getByName(ec2IpAddress);
        if (address.isReachable(5000)) {
            logger.info("Host is reachable");
        } else {
            logger.error("Host is not reachable: {}", ec2IpAddress);
            throw new IOException("Host is not reachable: " + ec2IpAddress);
        }
    }

    private void configureSession(Session session) {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
    }

    private void disconnectFromEC2(Session session) {
        if (session != null && session.isConnected()) {
            logger.info("Disconnecting from EC2...");
            session.disconnect();
            logger.info("Disconnected successfully");
        }
    }

    private String executeCommand(Session session, String command) throws JSchException, IOException {
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

            if (!error.isEmpty() && !error.contains("% Total    % Received % Xferd")) {
                logger.error("Error executing command: {}", error);
                throw new JSchException("Error executing command: " + error);
            }

            logger.info("Command executed successfully. Response: {}", response);
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
