package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.BlockChainNetworkRequest;
import Capstone.Capstone.controller.dto.BlockChainNetworkResponse;
import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.repository.AWSVmInfoRepository;
import Capstone.Capstone.repository.AzureVmInfoRepository;
import Capstone.Capstone.repository.OpenstackCloudInfoRepository;
import Capstone.Capstone.repository.UserRepository;
import com.jcraft.jsch.Session;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BlockChainNetworkService {

    private final SSHConnector sshConnector;
    private final AWSVmInfoRepository awsVmInfoRepository;
    private final AzureVmInfoRepository azureVmInfoRepository;
    private final OpenstackCloudInfoRepository openstackCloudInfoRepository;
    private final UserRepository userRepository;

    public BlockChainNetworkService(SSHConnector sshConnector,
        AWSVmInfoRepository awsVmInfoRepository,
        AzureVmInfoRepository azureVmInfoRepository,
        OpenstackCloudInfoRepository openstackCloudInfoRepository, UserRepository userRepository) {
        this.sshConnector = sshConnector;
        this.awsVmInfoRepository = awsVmInfoRepository;
        this.azureVmInfoRepository = azureVmInfoRepository;
        this.openstackCloudInfoRepository = openstackCloudInfoRepository;
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

    @Transactional
    public BlockChainNetworkResponse executeStartupScript(BlockChainNetworkRequest network) {
        try {
            // CA VM 설정
            setupCAVM(network);

            // ORG1 VM 설정
            setupORG1VM(network);

            return new BlockChainNetworkResponse(network.getUserId(),network.getNetworkName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupCAVM(BlockChainNetworkRequest network) throws Exception {
        Session session = sshConnector.connectToEC2(network.getCaSecretKey(), network.getCaIP());
        try {
            // startup-ca.sh 다운로드 및 실행
            sshConnector.executeCommand(session, "curl -L -o $PWD/startup-ca.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-ca.sh");
            sshConnector.executeCommand(session, "chmod +x startup-ca.sh");
            sshConnector.executeCommand(session, "./startup-ca.sh " + network.getCaIP());
        } finally {
            sshConnector.disconnectFromEC2(session);
        }
    }

    private void setupORG1VM(BlockChainNetworkRequest network) throws Exception {
        Session session = sshConnector.connectToEC2(network.getCaSecretKey(), network.getOrgIP());
        try {
            // temp.pem 파일 전송
            sshConnector.sendPemKeyViaSftp(session, network.getCaSecretKey());

            // chmod 400 temp.pem
            sshConnector.executeCommand(session, "chmod 400 temp.pem");

            // startup-org1.sh 다운로드 및 실행
            sshConnector.executeCommand(session, "curl -L -o $PWD/startup-org1.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-org1.sh");
            sshConnector.executeCommand(session, "chmod +x startup-org1.sh");
            sshConnector.executeCommand(session, "./startup-org1.sh " + network.getCaIP());
        } finally {
            sshConnector.disconnectFromEC2(session);
        }
    }


}
