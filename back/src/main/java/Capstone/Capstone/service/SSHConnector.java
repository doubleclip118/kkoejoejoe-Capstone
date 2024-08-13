package Capstone.Capstone.service;

import com.jcraft.jsch.*;
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

    static {
        JSch.setLogger(new com.jcraft.jsch.Logger() {
            @Override
            public boolean isEnabled(int level) { return true; }
            @Override
            public void log(int level, String message) {
                System.out.println("JSch Log: " + message);
            }
        });
    }

    public Session connectToEC2(String privateKeyContent, String ec2IpAddress) throws Exception {
        Path tempKeyFile = null;
        try {
            String formattedKey = formatPrivateKey(privateKeyContent);
            tempKeyFile = createTempKeyFile(formattedKey);

            JSch jsch = new JSch();
            jsch.addIdentity(tempKeyFile.toString());

            String user = "ubuntu";
            int port = 22;

            System.out.println("Connecting to " + ec2IpAddress + " with user " + user);

            // 네트워크 연결 테스트
            testNetworkConnection(ec2IpAddress);

            Session session = jsch.getSession(user, ec2IpAddress, port);

            // SSH 연결 설정
            configureSession(session);

            System.out.println("Attempting to connect...");
            session.connect(30000); // 30초 타임아웃 설정
            System.out.println("Connected successfully");

            return session;
        } finally {
            if (tempKeyFile != null) {
                Files.deleteIfExists(tempKeyFile);
            }
        }
    }

    private String formatPrivateKey(String key) {
        if (!key.startsWith("-----BEGIN RSA PRIVATE KEY-----")) {
            key = "-----BEGIN RSA PRIVATE KEY-----\n" + key;
        }
        if (!key.endsWith("-----END RSA PRIVATE KEY-----")) {
            key = key + "\n-----END RSA PRIVATE KEY-----";
        }
        return key.replace("\\n", "\n");
    }

    private Path createTempKeyFile(String key) throws IOException {
        Path tempKeyFile = Files.createTempFile("temp", ".pem");
        Files.write(tempKeyFile, key.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
        Files.setPosixFilePermissions(tempKeyFile, perms);
        return tempKeyFile;
    }

    private void testNetworkConnection(String ec2IpAddress) throws IOException {
        InetAddress address = InetAddress.getByName(ec2IpAddress);
        if (address.isReachable(5000)) {
            System.out.println("Host is reachable");
        } else {
            System.out.println("Host is not reachable");
            throw new IOException("Host is not reachable: " + ec2IpAddress);
        }
    }

    private void configureSession(Session session) {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
    }

    public void disconnectFromEC2(Session session) {
        if (session != null && session.isConnected()) {
            System.out.println("Disconnecting from EC2...");
            session.disconnect();
            System.out.println("Disconnected successfully");
        }
    }

    public String executeCommand(Session session, String command) throws Exception {
        System.out.println("Executing command: " + command);
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
                Thread.sleep(100);
            }

            String response = new String(responseStream.toByteArray());
            String error = new String(errorStream.toByteArray());

            if (!error.isEmpty()) {
                System.err.println("Error executing command: " + error);
                throw new RuntimeException("Error executing command: " + error);
            }

            System.out.println("Command executed successfully");
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
        String remotePemPath = "/home/ubuntu";
        try {
            String formattedKey = formatPrivateKey(privateKeyContent);
            tempKeyFile = createTempKeyFile(formattedKey);

            System.out.println("Opening SFTP channel...");
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            System.out.println("SFTP channel opened.");

            System.out.println("Sending PEM key file to " + remotePemPath);
            channelSftp.put(tempKeyFile.toString(), remotePemPath);
            System.out.println("PEM key file sent successfully.");

            // Set appropriate permissions for the PEM key file
            channelSftp.chmod(0600, remotePemPath);
            System.out.println("PEM key file permissions set to 600.");

        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
                System.out.println("SFTP channel closed.");
            }
            if (tempKeyFile != null) {
                Files.deleteIfExists(tempKeyFile);
                System.out.println("Temporary key file deleted.");
            }
        }
    }
}
