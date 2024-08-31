package Capstone.Capstone.service;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

@Service
public class SSHsshdConnector {
    private static final Logger logger = LoggerFactory.getLogger(SSHsshdConnector.class);

    public ClientSession connectToEC2(String privateKeyContent, String ec2IpAddress) throws IOException {
        SshClient client = SshClient.setUpDefaultClient();
        client.start();

        try {
            ClientSession session = client.connect("ubuntu", ec2IpAddress, 22)
                .verify(30, TimeUnit.SECONDS)
                .getSession();

            session.addPublicKeyIdentity(loadKeyPair(privateKeyContent));
            session.auth().verify(30, TimeUnit.SECONDS);

            logger.info("Connected successfully to {}", ec2IpAddress);
            return session;
        } catch (Exception e) {
            client.stop();
            throw new IOException("Failed to connect to EC2", e);
        }
    }

    private KeyPair loadKeyPair(String privateKeyContent) throws Exception {
        try (InputStream is = new ByteArrayInputStream(privateKeyContent.getBytes())) {
            Iterable<KeyPair> keyPairs = SecurityUtils.loadKeyPairIdentities(null, null, is, null);
            return keyPairs.iterator().next();
        }
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

    public void sendPemKeyViaSftp(ClientSession session, String privateKeyContent) throws IOException {
        Path tempKeyFile = null;
        try {
            tempKeyFile = Files.createTempFile("temp_key", ".pem");
            Files.write(tempKeyFile, privateKeyContent.getBytes());

            try (SftpClient sftpClient = SftpClientFactory.instance().createSftpClient(session);
                InputStream is = Files.newInputStream(tempKeyFile)) {

                String remoteFileName = "temp_key.pem";
                String remotePemPath = "/home/ubuntu/" + remoteFileName;

                logger.info("Sending PEM key file to {}", remotePemPath);

                // SFTP 파일 업로드
                try (SftpClient.CloseableHandle handle = sftpClient.open(remotePemPath, EnumSet.of(SftpClient.OpenMode.Write, SftpClient.OpenMode.Create))) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    long offset = 0;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        sftpClient.write(handle, offset, buffer, 0, bytesRead);
                        offset += bytesRead;
                    }
                }

                // 파일 권한 설정 (0600)
                SftpClient.Attributes attributes = sftpClient.stat(remotePemPath);
                attributes.setPermissions(0600);
                sftpClient.setStat(remotePemPath, attributes);

                logger.info("PEM key file sent successfully and permissions set to 600.");
            }
        } finally {
            if (tempKeyFile != null) {
                Files.deleteIfExists(tempKeyFile);
                logger.info("Temporary key file deleted.");
            }
        }
    }

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
