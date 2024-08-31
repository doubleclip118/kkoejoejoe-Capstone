package Capstone.Capstone.service;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

@Service
public class SSHsshdConnector {
    private static final Logger logger = LoggerFactory.getLogger(SSHsshdConnector.class);

    public ClientSession connectToEC2(String privateKeyContent, String ec2IpAddress) throws IOException {
        SshClient client = SshClient.setUpDefaultClient();
        client.start();

        try {
            logger.info("Attempting to connect to EC2: {}", ec2IpAddress);
            ClientSession session = client.connect("ubuntu", ec2IpAddress, 22)
                .verify(30, TimeUnit.SECONDS)
                .getSession();

            logger.info("Session created, attempting to load key pair");
            KeyPair keyPair = loadKeyPair(privateKeyContent);
            logger.info("Key pair loaded successfully");

            logger.info("Attempting authentication");
            session.addPublicKeyIdentity(keyPair);
            session.auth().verify(30, TimeUnit.SECONDS);

            logger.info("Connected successfully to {}", ec2IpAddress);
            return session;
        } catch (Exception e) {
            logger.error("Failed to connect to EC2: {}", e.getMessage(), e);
            client.stop();
            throw new IOException("Failed to connect to EC2", e);
        }
    }

    private KeyPair loadKeyPair(String privateKeyContent) throws Exception {
        String privateKeyPEM = privateKeyContent
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replaceAll(System.lineSeparator(), "")
            .replace("-----END RSA PRIVATE KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        return new KeyPair(null, privateKey);
    }

    public String executeCommand(ClientSession session, String command) throws IOException {
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        try (ClientChannel channel = session.createExecChannel(command)) {
            channel.setOut(responseStream);
            channel.setErr(errorStream);
            channel.open().verify(30, TimeUnit.SECONDS);
            channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(30));

            String response = responseStream.toString(StandardCharsets.UTF_8);
            String error = errorStream.toString(StandardCharsets.UTF_8);

            if (!error.isEmpty() && !error.contains("% Total    % Received % Xferd")) {
                logger.error("Error executing command: {}", error);
                throw new IOException("Error executing command: " + error);
            }

            logger.info("Command executed successfully. Response: {}", response);
            return response;
        }
    }

//    public void sendPemKeyViaSftp(ClientSession session, String privateKeyContent) throws IOException {
//        Path tempKeyFile = null;
//        try {
//            tempKeyFile = Files.createTempFile("temp_key", ".pem");
//            Files.write(tempKeyFile, privateKeyContent.getBytes());
//
//            try (SftpClient sftpClient = SftpClientFactory.instance().createSftpClient(session)) {
//                String remoteFileName = "temp_key.pem";
//                String remotePemPath = "/home/ubuntu/" + remoteFileName;
//
//                logger.info("Sending PEM key file to {}", remotePemPath);
//                try (InputStream is = Files.newInputStream(tempKeyFile)) {
//                    sftpClient.write(remotePemPath, is);
//                }
//
//                // Set file permissions (0600)
//                sftpClient.setAttribute(remotePemPath,
//                    new SftpClient.Attributes().withPermissions(0600));
//                logger.info("PEM key file sent successfully and permissions set to 600.");
//            }
//        } finally {
//            if (tempKeyFile != null) {
//                Files.deleteIfExists(tempKeyFile);
//                logger.info("Temporary key file deleted.");
//            }
//        }
//    }

    public void disconnectFromEC2(ClientSession session) {
        if (session != null) {
            try {
                session.close();
                logger.info("Disconnected successfully");
            } catch (IOException e) {
                logger.error("Error disconnecting from EC2", e);
            }
        }
    }
}
