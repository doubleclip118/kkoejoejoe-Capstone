package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.*;
import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.domain.SecurityGroupRule;
import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.AWSVmInfoRepository;
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
public class AWSVmInfoService {
    private final AWSVmInfoRepository awsVmInfoRepository;
    private final UserRepository userRepository;
    private final SecurityGroupRuleRepository securityGroupRuleRepository;
    private final ExternalApiService externalApiService;

    public AWSVmInfoService(AWSVmInfoRepository awsVmInfoRepository, UserRepository userRepository,
        SecurityGroupRuleRepository securityGroupRuleRepository,
        ExternalApiService externalApiService) {
        this.awsVmInfoRepository = awsVmInfoRepository;
        this.userRepository = userRepository;
        this.securityGroupRuleRepository = securityGroupRuleRepository;
        this.externalApiService = externalApiService;
    }

    @Transactional
    public VmInfoResponse createAWSVmInfo(VmInfoRequest vmInfoRequest) {
        User user = userRepository.findById(vmInfoRequest.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        AWSVmInfo awsVmInfo = new AWSVmInfo(
            user,
            vmInfoRequest.getConnectionName(),
            vmInfoRequest.getVmName(),
            vmInfoRequest.getVpcName(),
            vmInfoRequest.getVpcIPv4Cidr(),
            vmInfoRequest.getSubnetName(),
            vmInfoRequest.getSubnetIPv4Cidr(),
            vmInfoRequest.getSecurityGroupName(),
            new ArrayList<>(),  // SecurityGroupRules will be added separately
            vmInfoRequest.getKeypairName(),
            vmInfoRequest.getImageName(),
            vmInfoRequest.getVmSpec(),
            null,
            null
        );

        AWSVmInfo savedAWSVmInfo = awsVmInfoRepository.save(awsVmInfo);

        for (SecurityGroupRuleDTO ruleDTO : vmInfoRequest.getSecurityGroupRules()) {
            SecurityGroupRule rule = new SecurityGroupRule(
                savedAWSVmInfo,
                ruleDTO.getFromPort(),
                ruleDTO.getToPort(),
                ruleDTO.getIpProtocol(),
                ruleDTO.getDirection()
            );
            securityGroupRuleRepository.save(rule);
            savedAWSVmInfo.addSecurityGroupRule(rule);
        }

        return convertToVmInfoDTO(savedAWSVmInfo);
    }

    @Transactional
    public String deleteAWSVmInfo(Long id) {
        awsVmInfoRepository.deleteById(id);
        return "삭제 완료";
    }

    @Transactional
    public VmcreateResponse createVm(Long id) {
        AWSVmInfo vmInfo = awsVmInfoRepository.findById(id)
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
        awsVmInfoRepository.save(vmInfo);

        return new VmcreateResponse(vmInfo.getUserInfo().getId(), vmInfo.getId(), vmInfo.getSecretkey(), vmInfo.getIp());
    }
    @Transactional
    public String deleteVm(Long vmid){
        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmid).orElseThrow(
            () -> new VmInfoNotFoundException("Vm info Not Found")
        );
        externalApiService.deleteVm(vmInfo.getVmName(),vmInfo.getConnectionName());
        awsVmInfoRepository.deleteById(vmid);
        return "삭제 완료";
    }

    public List<GetVmDTO> getVmDTOList(Long id){
        User user = userRepository.findByUserIdWithVAndAwsVmInfos(id).orElseThrow(
            () -> new UserNotFoundException("User with vm not found")
        );
        return user.getAwsVmInfos().stream()
            .map(awsVmInfo -> new GetVmDTO(awsVmInfo.getId(), awsVmInfo.getVmName(),awsVmInfo.getIp(),new String(Base64.getDecoder().decode(awsVmInfo.getSecretkey()))))
            .collect(Collectors.toList());
    }

    private VmInfoResponse convertToVmInfoDTO(AWSVmInfo awsVmInfo) {
        VmInfoResponse dto = new VmInfoResponse();
        dto.setUserId(awsVmInfo.getUserInfo().getId());
        dto.setVmId(awsVmInfo.getId());
        dto.setConnectionName(awsVmInfo.getConnectionName());
        dto.setVmName(awsVmInfo.getVmName());
        dto.setVpcName(awsVmInfo.getVpcName());
        dto.setVpcIPv4Cidr(awsVmInfo.getVpcIPv4CIDR());
        dto.setSubnetName(awsVmInfo.getSubnetName());
        dto.setSubnetIPv4Cidr(awsVmInfo.getSubnetIPv4CIDR());
        dto.setSecurityGroupName(awsVmInfo.getSecurityGroupName());
        dto.setKeypairName(awsVmInfo.getKeypairName());
        dto.setImageName(awsVmInfo.getImageName());
        dto.setVmSpec(awsVmInfo.getVmSpec());

        List<SecurityGroupRuleDTO> ruleDTOs = awsVmInfo.getSecurityGroupRules().stream()
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

    private CreateVPCRequestDTO prepareVPCRequest(AWSVmInfo vmInfo) {
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

    private CreateSecurityGroupRequestDTO prepareSecurityGroupRequest(AWSVmInfo vmInfo, String vpcName) {
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

    private CreateKeyPairRequestDTO prepareKeyPairRequest(AWSVmInfo vmInfo) {
        CreateKeyPairRequestDTO.ReqInfo reqInfo = new CreateKeyPairRequestDTO.ReqInfo();
        reqInfo.setName(vmInfo.getKeypairName());

        CreateKeyPairRequestDTO createKeyPairRequestDTO = new CreateKeyPairRequestDTO();
        createKeyPairRequestDTO.setConnectionName(vmInfo.getConnectionName());
        createKeyPairRequestDTO.setReqInfo(reqInfo);

        return createKeyPairRequestDTO;
    }

    private CreateVMRequestDTO prepareVMRequest(AWSVmInfo vmInfo, String vpcName, String securityGroupName, String keyPairName) {
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
