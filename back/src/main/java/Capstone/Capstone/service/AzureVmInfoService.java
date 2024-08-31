package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.*;
import Capstone.Capstone.domain.AzureVmInfo;
import Capstone.Capstone.domain.SecurityGroupRule;
import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.AzureVmInfoRepository;
import Capstone.Capstone.repository.SecurityGroupRuleRepository;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.service.dto.*;
import Capstone.Capstone.utils.error.UserNotFoundException;
import Capstone.Capstone.utils.error.VmInfoNotFoundException;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AzureVmInfoService {
    private final AzureVmInfoRepository azureVmInfoRepository;
    private final UserRepository userRepository;
    private final SecurityGroupRuleRepository securityGroupRuleRepository;
    private final ExternalApiService externalApiService;

    public AzureVmInfoService(AzureVmInfoRepository azureVmInfoRepository, UserRepository userRepository,
        SecurityGroupRuleRepository securityGroupRuleRepository,
        ExternalApiService externalApiService) {
        this.azureVmInfoRepository = azureVmInfoRepository;
        this.userRepository = userRepository;
        this.securityGroupRuleRepository = securityGroupRuleRepository;
        this.externalApiService = externalApiService;
    }

    @Transactional
    public VmInfoResponse createAzureVmInfo(VmInfoRequest vmInfoRequest) {
        User user = userRepository.findById(vmInfoRequest.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        AzureVmInfo azureVmInfo = new AzureVmInfo(
            user,
            vmInfoRequest.getConnectionName(),
            vmInfoRequest.getVmName(),
            vmInfoRequest.getVpcName(),
            vmInfoRequest.getVpcIPv4Cidr(),
            vmInfoRequest.getSubnetName(),
            vmInfoRequest.getSubnetIPv4Cidr(),
            vmInfoRequest.getSecurityGroupName(),
            new ArrayList<>(),
            vmInfoRequest.getKeypairName(),
            vmInfoRequest.getImageName(),
            vmInfoRequest.getVmSpec(),
            null,
            null
        );

        AzureVmInfo savedAzureVmInfo = azureVmInfoRepository.save(azureVmInfo);

        for (SecurityGroupRuleDTO ruleDTO : vmInfoRequest.getSecurityGroupRules()) {
            SecurityGroupRule rule = new SecurityGroupRule(
                savedAzureVmInfo,
                ruleDTO.getFromPort(),
                ruleDTO.getToPort(),
                ruleDTO.getIpProtocol(),
                ruleDTO.getDirection()
            );
            securityGroupRuleRepository.save(rule);
            savedAzureVmInfo.addSecurityGroupRule(rule);
        }

        return convertToVmInfoDTO(savedAzureVmInfo);
    }

    @Transactional
    public String deleteAzureVmInfo(Long id) {
        azureVmInfoRepository.deleteById(id);
        return "삭제 완료";
    }

    @Transactional
    public VmcreateResponse createVm(Long id) {
        AzureVmInfo vmInfo = azureVmInfoRepository.findById(id)
            .orElseThrow(() -> new VmInfoNotFoundException("VM Not Found"));

        CreateVPCRequestDTO createVPCRequestDTO = prepareVPCRequest(vmInfo);
        externalApiService.createVPC(createVPCRequestDTO);

        CreateSecurityGroupRequestDTO createSGRequestDTO = prepareSecurityGroupRequest(vmInfo, vmInfo.getVpcName());
        CreateSecurityGroupResponseDTO sgResponse = externalApiService.createSecurityGroup(createSGRequestDTO);

        CreateKeyPairRequestDTO createKeyPairRequestDTO = prepareKeyPairRequest(vmInfo);
        CreateKeyPairResponseDTO keyPairResponse = externalApiService.createKeypair(createKeyPairRequestDTO);

        vmInfo.setSecretkey(Base64.getEncoder().encodeToString(keyPairResponse.getPrivateKey().getBytes()));


        CreateVMRequestDTO createVMRequestDTO = prepareVMRequest(vmInfo, vmInfo.getVpcName(), vmInfo.getSecurityGroupName(), vmInfo.getKeypairName());
        CreateVMResponseDTO vmResponse = externalApiService.createVM(createVMRequestDTO);

        vmInfo.setIp(vmResponse.getPublicIP());
        azureVmInfoRepository.save(vmInfo);

        return new VmcreateResponse(vmInfo.getUserInfo().getId(), vmInfo.getId(), vmInfo.getSecretkey(), vmInfo.getIp());
    }

    @Transactional
    public String deleteVm(Long vmid) {
        AzureVmInfo vmInfo = azureVmInfoRepository.findById(vmid).orElseThrow(
            () -> new VmInfoNotFoundException("Vm info Not Found")
        );
        externalApiService.deleteVm(vmInfo.getVmName(),vmInfo.getConnectionName());
        azureVmInfoRepository.deleteById(vmid);
        return "삭제 완료";
    }

    public List<GetVmDTO> getVmDTOList(Long id) {
        User user = userRepository.findByUserIdWithVAndAzureVmInfos(id).orElseThrow(
            () -> new UserNotFoundException("User with vm not found")
        );
        return user.getAzureVmInfos().stream()
            .map(azureVmInfo -> new GetVmDTO(azureVmInfo.getId(), azureVmInfo.getVmName(),azureVmInfo.getIp(),new String(Base64.getDecoder().decode(azureVmInfo.getSecretkey()))))
            .collect(Collectors.toList());
    }

    private VmInfoResponse convertToVmInfoDTO(AzureVmInfo azureVmInfo) {
        VmInfoResponse dto = new VmInfoResponse();
        dto.setUserId(azureVmInfo.getUserInfo().getId());
        dto.setVmId(azureVmInfo.getId());
        dto.setConnectionName(azureVmInfo.getConnectionName());
        dto.setVmName(azureVmInfo.getVmName());
        dto.setVpcName(azureVmInfo.getVpcName());
        dto.setVpcIPv4Cidr(azureVmInfo.getVpcIPv4CIDR());
        dto.setSubnetName(azureVmInfo.getSubnetName());
        dto.setSubnetIPv4Cidr(azureVmInfo.getSubnetIPv4CIDR());
        dto.setSecurityGroupName(azureVmInfo.getSecurityGroupName());
        dto.setKeypairName(azureVmInfo.getKeypairName());
        dto.setImageName(azureVmInfo.getImageName());
        dto.setVmSpec(azureVmInfo.getVmSpec());


        List<SecurityGroupRuleDTO> ruleDTOs = azureVmInfo.getSecurityGroupRules().stream()
            .map(this::convertToSecurityGroupRuleDTO)
            .collect(Collectors.toList());
        dto.setSecurityGroupRules(ruleDTOs);

        return dto;
    }

    private SecurityGroupRuleDTO convertToSecurityGroupRuleDTO(SecurityGroupRule rule) {
        SecurityGroupRuleDTO dto = new SecurityGroupRuleDTO();
        dto.setFromPort(rule.getFromPort());
        dto.setToPort(rule.getToPort());
        dto.setIpProtocol(rule.getIpProtocol());
        dto.setDirection(rule.getDirection());
        return dto;
    }

    private CreateVPCRequestDTO prepareVPCRequest(AzureVmInfo vmInfo) {
        CreateVPCRequestDTO.ReqInfo reqInfo = new CreateVPCRequestDTO.ReqInfo();
        reqInfo.setName(vmInfo.getVpcName());
        reqInfo.setIPv4CIDR(vmInfo.getVpcIPv4CIDR());

        CreateVPCRequestDTO.SubnetInfo subnetInfo = new CreateVPCRequestDTO.SubnetInfo();
        subnetInfo.setName(vmInfo.getSubnetName());
        subnetInfo.setIPv4CIDR(vmInfo.getSubnetIPv4CIDR());

        List<CreateVPCRequestDTO.SubnetInfo> subnetInfoList = new ArrayList<>();
        subnetInfoList.add(subnetInfo);
        reqInfo.setSubnetInfoList(subnetInfoList);

        CreateVPCRequestDTO createVPCRequestDTO = new CreateVPCRequestDTO();
        createVPCRequestDTO.setConnectionName(vmInfo.getConnectionName());
        createVPCRequestDTO.setReqInfo(reqInfo);

        return createVPCRequestDTO;
    }

    private CreateSecurityGroupRequestDTO prepareSecurityGroupRequest(AzureVmInfo vmInfo, String vpcName) {
        CreateSecurityGroupRequestDTO.ReqInfo reqInfo = new CreateSecurityGroupRequestDTO.ReqInfo();
        reqInfo.setName(vmInfo.getSecurityGroupName());
        reqInfo.setVpcName(vpcName);

        List<CreateSecurityGroupRequestDTO.SecurityRule> securityRules = vmInfo.getSecurityGroupRules().stream()
            .map(rule -> new CreateSecurityGroupRequestDTO.SecurityRule(
                rule.getFromPort(),
                rule.getToPort(),
                rule.getIpProtocol(),
                rule.getDirection()
            ))
            .collect(Collectors.toList());

        reqInfo.setSecurityRules(securityRules);

        CreateSecurityGroupRequestDTO createSGRequestDTO = new CreateSecurityGroupRequestDTO();
        createSGRequestDTO.setConnectionName(vmInfo.getConnectionName());
        createSGRequestDTO.setReqInfo(reqInfo);

        return createSGRequestDTO;
    }

    private CreateKeyPairRequestDTO prepareKeyPairRequest(AzureVmInfo vmInfo) {
        CreateKeyPairRequestDTO.ReqInfo reqInfo = new CreateKeyPairRequestDTO.ReqInfo();
        reqInfo.setName(vmInfo.getKeypairName());

        CreateKeyPairRequestDTO createKeyPairRequestDTO = new CreateKeyPairRequestDTO();
        createKeyPairRequestDTO.setConnectionName(vmInfo.getConnectionName());
        createKeyPairRequestDTO.setReqInfo(reqInfo);

        return createKeyPairRequestDTO;
    }

    private CreateVMRequestDTO prepareVMRequest(AzureVmInfo vmInfo, String vpcName, String securityGroupName, String keyPairName) {
        CreateVMRequestDTO.ReqInfo reqInfo = new CreateVMRequestDTO.ReqInfo();
        reqInfo.setName(vmInfo.getVmName());
        reqInfo.setImageName(vmInfo.getImageName());
        reqInfo.setVpcName(vpcName);
        reqInfo.setSubnetName(vmInfo.getSubnetName());
        reqInfo.setSecurityGroupNames(List.of(securityGroupName));
        reqInfo.setVmSpecName(vmInfo.getVmSpec());
        reqInfo.setKeyPairName(keyPairName);

        CreateVMRequestDTO createVMRequestDTO = new CreateVMRequestDTO();
        createVMRequestDTO.setConnectionName(vmInfo.getConnectionName());
        createVMRequestDTO.setReqInfo(reqInfo);

        return createVMRequestDTO;
    }
}
