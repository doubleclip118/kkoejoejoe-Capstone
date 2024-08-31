//package Capstone.Capstone.service;
//
//import Capstone.Capstone.controller.dto.BlockChainNetworkRequest;
//import Capstone.Capstone.controller.dto.BlockChainNetworkResponse;
//import Capstone.Capstone.domain.AWSVmInfo;
//import Capstone.Capstone.domain.BlockChainNetwork;
//import Capstone.Capstone.domain.User;
//import Capstone.Capstone.repository.AWSVmInfoRepository;
//import Capstone.Capstone.repository.AzureVmInfoRepository;
//import Capstone.Capstone.repository.BlockChainNetworkRepository;
//import Capstone.Capstone.repository.OpenstackCloudInfoRepository;
//import Capstone.Capstone.repository.UserRepository;
//import Capstone.Capstone.utils.error.UserNotFoundException;
//import org.apache.sshd.client.session.ClientSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import jakarta.transaction.Transactional;
//
//@Service
//public class BlockChainNetworkService {
//
//    private static final Logger logger = LoggerFactory.getLogger(BlockChainNetworkService.class);
//
//    private final SSHsshdConnector sshConnector;
//    private final AWSVmInfoRepository awsVmInfoRepository;
//    private final AzureVmInfoRepository azureVmInfoRepository;
//    private final OpenstackCloudInfoRepository openstackCloudInfoRepository;
//    private final UserRepository userRepository;
//    private final BlockChainNetworkRepository blockChainNetworkRepository;
//
//    public BlockChainNetworkService(SSHsshdConnector sshConnector,
//        AWSVmInfoRepository awsVmInfoRepository,
//        AzureVmInfoRepository azureVmInfoRepository,
//        OpenstackCloudInfoRepository openstackCloudInfoRepository,
//        UserRepository userRepository,
//        BlockChainNetworkRepository blockChainNetworkRepository) {
//        this.sshConnector = sshConnector;
//        this.awsVmInfoRepository = awsVmInfoRepository;
//        this.azureVmInfoRepository = azureVmInfoRepository;
//        this.openstackCloudInfoRepository = openstackCloudInfoRepository;
//        this.userRepository = userRepository;
//        this.blockChainNetworkRepository = blockChainNetworkRepository;
//    }
//
//    @Transactional
//    public void connectToEC2Instance(Long vmId) {
//        logger.info("Attempting to connect to EC2 instance with ID: {}", vmId);
//        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmId)
//            .orElseThrow(() -> new RuntimeException("VM not found with ID: " + vmId));
//
//        String privateKey = vmInfo.getSecretkey();
//        String ipAddress = vmInfo.getIp();
//
//        ClientSession session = null;
//        try {
//            session = sshConnector.connectToEC2(privateKey, ipAddress);
//            logger.info("Successfully connected to EC2 instance: {}", ipAddress);
//
//            String command = "ls -al";
//            String result = sshConnector.executeCommand(session, command);
//            logger.info("Command result: {}", result);
//
//        } catch (Exception e) {
//            logger.error("Failed to connect to EC2 instance: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed to connect to EC2 instance", e);
//        } finally {
//            sshConnector.disconnectFromEC2(session);
//        }
//    }
//
//    @Transactional
//    public void sftpToEC2Instance(Long vmId) {
//        logger.info("Attempting SFTP to EC2 instance with ID: {}", vmId);
//        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmId)
//            .orElseThrow(() -> new RuntimeException("VM not found with ID: " + vmId));
//
//        String privateKey = vmInfo.getSecretkey();
//        String ipAddress = vmInfo.getIp();
//
//        ClientSession session = null;
//        try {
//            session = sshConnector.connectToEC2(privateKey, ipAddress);
//            logger.info("Successfully connected to EC2 instance: {}", ipAddress);
//
//            sshConnector.sendPemKeyViaSftp(session, privateKey);
//            logger.info("Successfully sent PEM key via SFTP");
//
//        } catch (Exception e) {
//            logger.error("Failed SFTP to EC2 instance: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed SFTP to EC2 instance", e);
//        } finally {
//            sshConnector.disconnectFromEC2(session);
//        }
//    }
//
//    @Transactional
//    public BlockChainNetworkResponse executeStartupScript(BlockChainNetworkRequest network) {
//        logger.info("Executing startup script for network: {}", network.getNetworkName());
//        try {
//            setupCAVM(network);
//            setupORG1VM(network);
//
//            logger.info("Startup script executed successfully for network: {}", network.getNetworkName());
//            User user = userRepository.findById(network.getUserId()).orElseThrow(
//                () -> new UserNotFoundException("User Not Found")
//            );
//            BlockChainNetwork blockChainNetwork = new BlockChainNetwork(network.getNetworkName(),
//                network.getCaCSP(), network.getCaIP(), network.getCaSecretKey(),
//                network.getOrgCSP(), network.getOrgIP(), network.getOrgSecretKey(), user);
//            blockChainNetworkRepository.save(blockChainNetwork);
//            return new BlockChainNetworkResponse(network.getUserId(), network.getNetworkName());
//        } catch (Exception e) {
//            logger.error("Failed to execute startup script for network: {}", network.getNetworkName(), e);
//            throw new RuntimeException("Failed to execute startup script", e);
//        }
//    }
//
//    private void setupCAVM(BlockChainNetworkRequest network) throws Exception {
//        logger.info("Setting up CA VM for network: {}", network.getNetworkName());
//        ClientSession session = null;
//        try {
//            session = sshConnector.connectToEC2(network.getCaSecretKey(), network.getCaIP());
//
//            executeCommand(session, "curl -L -o -x  $PWD/startup-ca.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-ca.sh");
//            executeCommand(session, "./startup-ca.sh " + network.getCaIP());
//
//            logger.info("CA VM setup completed for network: {}", network.getNetworkName());
//        } finally {
//            sshConnector.disconnectFromEC2(session);
//        }
//    }
//
//    private void setupORG1VM(BlockChainNetworkRequest network) throws Exception {
//        logger.info("Setting up ORG1 VM for network: {}", network.getNetworkName());
//        ClientSession session = null;
//        try {
//            session = sshConnector.connectToEC2(network.getOrgSecretKey(), network.getOrgIP());
//
//            sshConnector.sendPemKeyViaSftp(session, network.getCaSecretKey());
//            executeCommand(session, "chmod 400 temp_key.pem");
//
//            executeCommand(session, "curl -L -o $PWD/startup-org1.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-org1.sh");
//            executeCommand(session, "chmod +x startup-org1.sh");
//            executeCommand(session, "./startup-org1.sh " + network.getCaIP());
//
//            logger.info("ORG1 VM setup completed for network: {}", network.getNetworkName());
//        } finally {
//            sshConnector.disconnectFromEC2(session);
//        }
//    }
//
//    private void executeCommand(ClientSession session, String command) throws Exception {
//        logger.debug("Executing command: {}", command);
//        String result = sshConnector.executeCommand(session, command);
//        logger.debug("Command result: {}", result);
//    }
//}
package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.BlockChainNetworkRequest;
import Capstone.Capstone.controller.dto.BlockChainNetworkResponse;
import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.domain.AzureVmInfo;
import Capstone.Capstone.domain.BlockChainNetwork;
import Capstone.Capstone.domain.OpenStackVmInfo;
import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.*;
import Capstone.Capstone.utils.error.NetworkNotFoundException;
import Capstone.Capstone.utils.error.UserNotFoundException;
import Capstone.Capstone.utils.error.VmInfoNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BlockChainNetworkService {

    private static final Logger logger = LoggerFactory.getLogger(BlockChainNetworkService.class);

    private final SSHConnector sshConnector;
    private final AWSVmInfoRepository awsVmInfoRepository;
    private final AzureVmInfoRepository azureVmInfoRepository;
    private final OpenStackVmInfoRepository openStackVmInfoRepository;
    private final UserRepository userRepository;
    private final BlockChainNetworkRepository blockChainNetworkRepository;
    private final ExternalApiService externalApiService;

    public BlockChainNetworkService(SSHConnector sshConnector,
        AWSVmInfoRepository awsVmInfoRepository,
        AzureVmInfoRepository azureVmInfoRepository,
        OpenStackVmInfoRepository openStackVmInfoRepository, UserRepository userRepository,
        BlockChainNetworkRepository blockChainNetworkRepository,
        ExternalApiService externalApiService) {
        this.sshConnector = sshConnector;
        this.awsVmInfoRepository = awsVmInfoRepository;
        this.azureVmInfoRepository = azureVmInfoRepository;
        this.openStackVmInfoRepository = openStackVmInfoRepository;
        this.userRepository = userRepository;
        this.blockChainNetworkRepository = blockChainNetworkRepository;
        this.externalApiService = externalApiService;
    }

    @Transactional
    public String deleteNetwork(Long networkId){
        BlockChainNetwork blockChainNetwork = blockChainNetworkRepository.findById(networkId)
            .orElseThrow(
                () -> new NetworkNotFoundException("Network Not Found")
            );
        String caIP = blockChainNetwork.getCaIP();
        String orgIP = blockChainNetwork.getOrgIP();
        if (blockChainNetwork.getCaCSP().equals("aws")){
            AWSVmInfo vmInfo = awsVmInfoRepository.findByIpIs(blockChainNetwork.getCaIP())
                .orElseThrow(
                    () -> new VmInfoNotFoundException("Vm Not Found")
                );
            externalApiService.deleteVm(vmInfo.getVmName(),vmInfo.getConnectionName());
            awsVmInfoRepository.deleteByIp(blockChainNetwork.getCaIP());
        }
        if (blockChainNetwork.getCaCSP().equals("azure")){
            AzureVmInfo vmInfo = azureVmInfoRepository.findByIpIs(blockChainNetwork.getCaIP())
                .orElseThrow(
                    () -> new VmInfoNotFoundException("Vm Not Found")
                );
            externalApiService.deleteVm(vmInfo.getVmName(),vmInfo.getConnectionName());
            azureVmInfoRepository.deleteByIp(blockChainNetwork.getCaIP());
        }
        if (blockChainNetwork.getCaCSP().equals("openstack")){
            OpenStackVmInfo vmInfo = openStackVmInfoRepository.findByIpIs(blockChainNetwork.getCaIP())
                .orElseThrow(
                    () -> new VmInfoNotFoundException("Vm Not Found")
                );
            externalApiService.deleteVm(vmInfo.getVmName(),vmInfo.getConnectionName());
            openStackVmInfoRepository.deleteByIp(blockChainNetwork.getCaIP());
        }
        if (blockChainNetwork.getOrgCSP().equals("aws")){
            AWSVmInfo vmInfo = awsVmInfoRepository.findByIpIs(blockChainNetwork.getOrgIP())
                .orElseThrow(
                    () -> new VmInfoNotFoundException("Vm Not Found")
                );
            externalApiService.deleteVm(vmInfo.getVmName(),vmInfo.getConnectionName());
            awsVmInfoRepository.deleteByIp(blockChainNetwork.getOrgIP());
        }
        if (blockChainNetwork.getOrgCSP().equals("azure")){
            AzureVmInfo vmInfo = azureVmInfoRepository.findByIpIs(blockChainNetwork.getOrgIP())
                .orElseThrow(
                    () -> new VmInfoNotFoundException("Vm Not Found")
                );
            externalApiService.deleteVm(vmInfo.getVmName(),vmInfo.getConnectionName());
            azureVmInfoRepository.deleteByIp(blockChainNetwork.getOrgIP());
        }
        if (blockChainNetwork.getOrgCSP().equals("openstack")){
            OpenStackVmInfo vmInfo = openStackVmInfoRepository.findByIpIs(blockChainNetwork.getOrgIP())
                .orElseThrow(
                    () -> new VmInfoNotFoundException("Vm Not Found")
                );
            externalApiService.deleteVm(vmInfo.getVmName(),vmInfo.getConnectionName());
            openStackVmInfoRepository.deleteByIp(blockChainNetwork.getOrgIP());
        }
        return "성공";
    }

    @Transactional
    public List<BlockChainNetworkResponse> getNetwork(Long userId){
        User user = userRepository.findByUserIdWithVAndbAndBlockChainNetworks(userId).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        return user.getBlockChainNetworks().stream().map(blockChainNetwork -> new BlockChainNetworkResponse(blockChainNetwork.getId(),blockChainNetwork.getNetworkName()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void sftpToEC2Instance(Long vmId) {
        logger.info("Attempting SFTP to EC2 instance with ID: {}", vmId);
        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmId)
            .orElseThrow(() -> new RuntimeException("VM not found with ID: " + vmId));

        String privateKey = vmInfo.getSecretkey();
        String ipAddress = vmInfo.getIp();

        try {
            sshConnector.sendPemKeyViaSftpWithNewSession(privateKey, ipAddress, privateKey);
            logger.info("Successfully sent PEM key via SFTP");
        } catch (Exception e) {
            logger.error("Failed SFTP to EC2 instance: {}", e.getMessage(), e);
            throw new RuntimeException("Failed SFTP to EC2 instance", e);
        }
    }

    @Transactional
    public BlockChainNetworkResponse executeStartupScript(BlockChainNetworkRequest network) {
        logger.info("Executing startup script for network: {}", network.getNetworkName());
        try {
            setupCAVM(network);
            setupORG1VM(network);

            logger.info("Startup script executed successfully for network: {}", network.getNetworkName());
            User user = userRepository.findById(network.getUserId()).orElseThrow(
                () -> new UserNotFoundException("User Not Found")
            );
            BlockChainNetwork blockChainNetwork = new BlockChainNetwork(network.getNetworkName(),
                network.getCaCSP(), network.getCaIP(), network.getCaSecretKey(),
                network.getOrgCSP(), network.getOrgIP(), network.getOrgSecretKey(), user);
            blockChainNetworkRepository.save(blockChainNetwork);
            return new BlockChainNetworkResponse(network.getUserId(), network.getNetworkName());
        } catch (Exception e) {
            logger.error("Failed to execute startup script for network: {}", network.getNetworkName(), e);
            throw new RuntimeException("Failed to execute startup script", e);
        }
    }

    private void setupCAVM(BlockChainNetworkRequest network) throws Exception {
        logger.info("Setting up CA VM for network: {}", network.getNetworkName());

        sshConnector.executeCommandWithNewSession(network.getCaSecretKey(), network.getCaIP(),
            "curl -L -o $PWD/startup-ca.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-ca.sh");
        sshConnector.executeCommandWithNewSession(network.getCaSecretKey(), network.getCaIP(),
            "chmod +x $PWD/startup-ca.sh");
        sshConnector.executeCommandWithNewSession(network.getCaSecretKey(), network.getCaIP(),
            "./startup-ca.sh " + network.getCaIP());

        logger.info("CA VM setup completed for network: {}", network.getNetworkName());
    }

    private void setupORG1VM(BlockChainNetworkRequest network) throws Exception {
        logger.info("Setting up ORG1 VM for network: {}", network.getNetworkName());

        sshConnector.sendPemKeyViaSftpWithNewSession(network.getOrgSecretKey(), network.getOrgIP(), network.getCaSecretKey());
        sshConnector.executeCommandWithNewSession(network.getOrgSecretKey(), network.getOrgIP(), "chmod 400 temp_key.pem");

        sshConnector.executeCommandWithNewSession(network.getOrgSecretKey(), network.getOrgIP(),
            "curl -L -o $PWD/startup-org1.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-org1.sh");
        sshConnector.executeCommandWithNewSession(network.getOrgSecretKey(), network.getOrgIP(), "chmod +x startup-org1.sh");
        sshConnector.executeCommandWithNewSession(network.getOrgSecretKey(), network.getOrgIP(), "./startup-org1.sh " + network.getCaIP());

        logger.info("ORG1 VM setup completed for network: {}", network.getNetworkName());
    }
}
//package Capstone.Capstone.service;
//
//import Capstone.Capstone.controller.dto.BlockChainNetworkRequest;
//import Capstone.Capstone.controller.dto.BlockChainNetworkResponse;
//import Capstone.Capstone.domain.AWSVmInfo;
//import Capstone.Capstone.domain.BlockChainNetwork;
//import Capstone.Capstone.domain.User;
//import Capstone.Capstone.repository.*;
//import Capstone.Capstone.utils.error.UserNotFoundException;
//import net.schmizz.sshj.SSHClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import jakarta.transaction.Transactional;
//import java.io.IOException;
//
//@Service
//public class BlockChainNetworkService {
//
//    private static final Logger logger = LoggerFactory.getLogger(BlockChainNetworkService.class);
//
//    private final SSHsshjConnector sshConnector;
//    private final AWSVmInfoRepository awsVmInfoRepository;
//    private final AzureVmInfoRepository azureVmInfoRepository;
//    private final OpenstackCloudInfoRepository openstackCloudInfoRepository;
//    private final UserRepository userRepository;
//    private final BlockChainNetworkRepository blockChainNetworkRepository;
//
//    public BlockChainNetworkService(SSHsshjConnector sshConnector,
//        AWSVmInfoRepository awsVmInfoRepository,
//        AzureVmInfoRepository azureVmInfoRepository,
//        OpenstackCloudInfoRepository openstackCloudInfoRepository,
//        UserRepository userRepository,
//        BlockChainNetworkRepository blockChainNetworkRepository) {
//        this.sshConnector = sshConnector;
//        this.awsVmInfoRepository = awsVmInfoRepository;
//        this.azureVmInfoRepository = azureVmInfoRepository;
//        this.openstackCloudInfoRepository = openstackCloudInfoRepository;
//        this.userRepository = userRepository;
//        this.blockChainNetworkRepository = blockChainNetworkRepository;
//    }
//
//    @Transactional
//    public void connectToEC2Instance(Long vmId) {
//        logger.info("Attempting to connect to EC2 instance with ID: {}", vmId);
//        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmId)
//            .orElseThrow(() -> new RuntimeException("VM not found with ID: " + vmId));
//
//        String privateKey = vmInfo.getSecretkey();
//        String ipAddress = vmInfo.getIp();
//
//        SSHClient ssh = null;
//        try {
//            ssh = sshConnector.connectToEC2(privateKey, ipAddress);
//            logger.info("Successfully connected to EC2 instance: {}", ipAddress);
//
//            String command = "ls -al";
//            String result = sshConnector.executeCommand(ssh, command);
//            logger.info("Command result: {}", result);
//
//        } catch (IOException e) {
//            logger.error("Failed to connect to EC2 instance: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed to connect to EC2 instance", e);
//        } finally {
//            sshConnector.disconnectFromEC2(ssh);
//        }
//    }
//
//    @Transactional
//    public void sftpToEC2Instance(Long vmId) {
//        logger.info("Attempting SFTP to EC2 instance with ID: {}", vmId);
//        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmId)
//            .orElseThrow(() -> new RuntimeException("VM not found with ID: " + vmId));
//
//        String privateKey = vmInfo.getSecretkey();
//        String ipAddress = vmInfo.getIp();
//
//        SSHClient ssh = null;
//        try {
//            ssh = sshConnector.connectToEC2(privateKey, ipAddress);
//            logger.info("Successfully connected to EC2 instance: {}", ipAddress);
//
//            sshConnector.sendPemKeyViaSftp(ssh, privateKey);
//            logger.info("Successfully sent PEM key via SFTP");
//
//        } catch (IOException e) {
//            logger.error("Failed SFTP to EC2 instance: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed SFTP to EC2 instance", e);
//        } finally {
//            sshConnector.disconnectFromEC2(ssh);
//        }
//    }
//
//    @Transactional
//    public BlockChainNetworkResponse executeStartupScript(BlockChainNetworkRequest network) {
//        logger.info("Executing startup script for network: {}", network.getNetworkName());
//        try {
//            setupCAVM(network);
//            setupORG1VM(network);
//
//            logger.info("Startup script executed successfully for network: {}", network.getNetworkName());
//            User user = userRepository.findById(network.getUserId()).orElseThrow(
//                () -> new UserNotFoundException("User Not Found")
//            );
//            BlockChainNetwork blockChainNetwork = new BlockChainNetwork(network.getNetworkName(),
//                network.getCaCSP(), network.getCaIP(), network.getCaSecretKey(),
//                network.getOrgCSP(), network.getOrgIP(), network.getOrgSecretKey(), user);
//            blockChainNetworkRepository.save(blockChainNetwork);
//            return new BlockChainNetworkResponse(network.getUserId(), network.getNetworkName());
//        } catch (Exception e) {
//            logger.error("Failed to execute startup script for network: {}", network.getNetworkName(), e);
//            throw new RuntimeException("Failed to execute startup script", e);
//        }
//    }
//
//    private void setupCAVM(BlockChainNetworkRequest network) throws IOException {
//        logger.info("Setting up CA VM for network: {}", network.getNetworkName());
//        SSHClient ssh = null;
//        try {
//            ssh = sshConnector.connectToEC2(network.getCaSecretKey(), network.getCaIP());
//
//            executeCommand(ssh, "curl -L -o $PWD/startup-ca.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-ca.sh");
//            executeCommand(ssh, "chmod +x startup-ca.sh");
//            executeCommand(ssh, "./startup-ca.sh " + network.getCaIP());
//
//            logger.info("CA VM setup completed for network: {}", network.getNetworkName());
//        } finally {
//            sshConnector.disconnectFromEC2(ssh);
//        }
//    }
//
//    private void setupORG1VM(BlockChainNetworkRequest network) throws IOException {
//        logger.info("Setting up ORG1 VM for network: {}", network.getNetworkName());
//        SSHClient ssh = null;
//        try {
//            ssh = sshConnector.connectToEC2(network.getOrgSecretKey(), network.getOrgIP());
//
//            sshConnector.sendPemKeyViaSftp(ssh, network.getCaSecretKey());
//            executeCommand(ssh, "chmod 400 temp_key.pem");
//
//            executeCommand(ssh, "curl -L -o $PWD/startup-org1.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-org1.sh");
//            executeCommand(ssh, "chmod +x startup-org1.sh");
//            executeCommand(ssh, "./startup-org1.sh " + network.getCaIP());
//
//            logger.info("ORG1 VM setup completed for network: {}", network.getNetworkName());
//        } finally {
//            sshConnector.disconnectFromEC2(ssh);
//        }
//    }
//
//    private void executeCommand(SSHClient ssh, String command) throws IOException {
//        logger.debug("Executing command: {}", command);
//        String result = sshConnector.executeCommand(ssh, command);
//        logger.debug("Command result: {}", result);
//    }
//}
