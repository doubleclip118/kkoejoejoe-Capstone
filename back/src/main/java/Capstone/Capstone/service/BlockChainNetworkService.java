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

        String privateKey = network.getSecretkey();
        String ipAddress = network.getIp();

        Session session = null;
        try {
            session = sshConnector.connectToEC2(privateKey, ipAddress);
            System.out.println("Successfully connected to EC2 instance: " + ipAddress);

            // Execute the curl command to download the script
            String curlCommand = "curl -L -o $PWD/startup-ca.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-ca.sh";
            sshConnector.executeCommand(session, curlCommand);
            System.out.println("Startup script downloaded successfully");

            // Set execute permissions for the script
            String chmodCommand = "chmod +x startup-ca.sh";
            sshConnector.executeCommand(session, chmodCommand);
            System.out.println("Execute permissions set for startup script");

            // Execute the startup script with the VM's IP address
            String executeCommand = "./startup-ca.sh " + ipAddress;
            String result = sshConnector.executeCommand(session, executeCommand);
            System.out.println("Startup script executed. Result: " + result);

        } catch (Exception e) {
            System.err.println("Failed to execute startup script on EC2 instance: " + e.getMessage());
        } finally {
            sshConnector.disconnectFromEC2(session);
        }
    }


}
