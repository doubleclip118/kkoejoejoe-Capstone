package Capstone.Capstone.service;

import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.repository.AWSVmInfoRepository;
import Capstone.Capstone.repository.UserRepository;
import com.jcraft.jsch.Session;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AWSVmNetworkService {

    private final SSHConnector sshConnector;
    private final AWSVmInfoRepository awsVmInfoRepository;
    private final UserRepository userRepository;

    public AWSVmNetworkService(SSHConnector sshConnector, AWSVmInfoRepository awsVmInfoRepository,
        UserRepository userRepository) {
        this.sshConnector = sshConnector;
        this.awsVmInfoRepository = awsVmInfoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void connectToEC2Instance(Long vmId) {
        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmId)
            .orElseThrow(() -> new RuntimeException("VM not found"));

        String privateKey = vmInfo.getSecretkey();
        String ipAddress = vmInfo.getIp();

        Session session = null;
        try {
            session = sshConnector.connectToEC2(privateKey, ipAddress);

            System.out.println("Successfully connected to EC2 instance: " + ipAddress);

            // 기본 명령어 실행
            String command = "ls -al";
            String result = sshConnector.executeCommand(session, command);
            System.out.println("Command result: " + result);

        } catch (Exception e) {
            System.err.println("Failed to connect to EC2 instance: " + e.getMessage());
        } finally {
            sshConnector.disconnectFromEC2(session);
        }
    }

    @Transactional
    public void sftpToEC2Instance(Long vmId) {
        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmId)
            .orElseThrow(() -> new RuntimeException("VM not found"));

        String privateKey = vmInfo.getSecretkey();
        String ipAddress = vmInfo.getIp();

        Session session = null;
        try {
            session = sshConnector.connectToEC2(privateKey, ipAddress);

            System.out.println("Successfully connected to EC2 instance: " + ipAddress);


            sshConnector.sendPemKeyViaSftp(session, privateKey);
            System.out.println("전송 성공");

        } catch (Exception e) {
            System.err.println("Failed to connect to EC2 instance: " + e.getMessage());
        } finally {
            sshConnector.disconnectFromEC2(session);
        }
    }


}
