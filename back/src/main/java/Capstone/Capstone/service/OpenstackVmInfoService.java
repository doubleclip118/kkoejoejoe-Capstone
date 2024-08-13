package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.*;
import Capstone.Capstone.domain.OpenStackVmInfo;
import Capstone.Capstone.domain.SecurityGroupRule;
import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.OpenStackVmInfoRepository;
import Capstone.Capstone.repository.SecurityGroupRuleRepository;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.service.dto.*;
import Capstone.Capstone.utils.error.UserNotFoundException;
import Capstone.Capstone.utils.error.VmInfoNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenstackVmInfoService {
    private final OpenStackVmInfoRepository openStackVmInfoRepository;
    private final UserRepository userRepository;
    private final SecurityGroupRuleRepository securityGroupRuleRepository;
    private final ExternalApiService externalApiService;

    public OpenstackVmInfoService(OpenStackVmInfoRepository openStackVmInfoRepository, UserRepository userRepository,
        SecurityGroupRuleRepository securityGroupRuleRepository,
        ExternalApiService externalApiService) {
        this.openStackVmInfoRepository = openStackVmInfoRepository;
        this.userRepository = userRepository;
        this.securityGroupRuleRepository = securityGroupRuleRepository;
        this.externalApiService = externalApiService;
    }

    @Transactional
    public VmInfoResponse createOpenStackVmInfo(VmInfoDTO vmInfoDTO) {
        User user = userRepository.findById(vmInfoDTO.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        OpenStackVmInfo openStackVmInfo = new OpenStackVmInfo(
            user,
            vmInfoDTO.getConnectionName(),
            vmInfoDTO.getVmName(),
            vmInfoDTO.getVpcName(),
            vmInfoDTO.getVpcIPv4Cidr(),
            vmInfoDTO.getSubnetName(),
            vmInfoDTO.getSubnetIPv4Cidr(),
            vmInfoDTO.getSecurityGroupName(),
            new ArrayList<>(),
            vmInfoDTO.getKeypairName(),
            vmInfoDTO.getImageName(),
            vmInfoDTO.getVmSpec(),
            null,
            null
        );

        OpenStackVmInfo savedOpenStackVmInfo = openStackVmInfoRepository.save(openStackVmInfo);

        for (SecurityGroupRuleDTO ruleDTO : vmInfoDTO.getSecurityGroupRules()) {
            SecurityGroupRule rule = new SecurityGroupRule(
                savedOpenStackVmInfo,
                ruleDTO.getFromPort(),
                ruleDTO.getToPort(),
                ruleDTO.getIpProtocol(),
                ruleDTO.getDirection()
            );
            securityGroupRuleRepository.save(rule);
            savedOpenStackVmInfo.addSecurityGroupRule(rule);
        }

        return convertToVmInfoResponse(savedOpenStackVmInfo);
    }

    @Transactional
    public String deleteOpenStackVmInfo(Long id) {
        openStackVmInfoRepository.deleteById(id);
        return "삭제 완료";
    }

    @Transactional
    public VmcreateResponse createVm(Long id) {
        OpenStackVmInfo vmInfo = openStackVmInfoRepository.findById(id)
            .orElseThrow(() -> new VmInfoNotFoundException("VM Not Found"));

        CreateVPCRequestDTO createVPCRequestDTO = prepareVPCRequest(vmInfo);
        externalApiService.createVPC(createVPCRequestDTO);

        CreateSecurityGroupRequestDTO createSGRequestDTO = prepareSecurityGroupRequest(vmInfo);
        CreateSecurityGroupResponseDTO sgResponse = externalApiService.createSecurityGroup(createSGRequestDTO);

        CreateKeyPairRequestDTO createKeyPairRequestDTO = prepareKeyPairRequest(vmInfo);
        CreateKeyPairResponseDTO keyPairResponse = externalApiService.createKeypair(createKeyPairRequestDTO);

        vmInfo.setSecretkey(keyPairResponse.getPrivateKey());

        CreateVMRequestDTO createVMRequestDTO = prepareVMRequest(vmInfo);
        CreateVMResponseDTO vmResponse = externalApiService.createVM(createVMRequestDTO);

        vmInfo.setIp(vmResponse.getPublicIP());
        openStackVmInfoRepository.save(vmInfo);

        return new VmcreateResponse(vmInfo.getUserInfo().getId(), vmInfo.getId(), vmInfo.getSecretkey(), vmInfo.getIp());
    }

    @Transactional
    public String deleteVm(Long vmid) {
        OpenStackVmInfo vmInfo = openStackVmInfoRepository.findById(vmid).orElseThrow(
            () -> new VmInfoNotFoundException("Vm info Not Found")
        );
        externalApiService.deleteVm(vmInfo.getVmName(), vmInfo.getConnectionName());
        openStackVmInfoRepository.deleteById(vmid);
        return "삭제 완료";
    }

    public List<GetVmDTO> getVmDTOList(Long id) {
        User user = userRepository.findByUserIdWithVAndOpenstackVmInfos(id).orElseThrow(
            () -> new UserNotFoundException("User with vm not found")
        );
        return user.getOpenStackVmInfos().stream()
            .map(openStackVmInfo -> new GetVmDTO(openStackVmInfo.getId(), openStackVmInfo.getVmName()))
            .collect(Collectors.toList());
    }

    private VmInfoResponse convertToVmInfoResponse(OpenStackVmInfo openStackVmInfo) {
        VmInfoResponse response = new VmInfoResponse();
        response.setUserId(openStackVmInfo.getUserInfo().getId());
        response.setVmId(openStackVmInfo.getId());
        response.setConnectionName(openStackVmInfo.getConnectionName());
        response.setVmName(openStackVmInfo.getVmName());
        response.setVpcName(openStackVmInfo.getVpcName());
        response.setVpcIPv4Cidr(openStackVmInfo.getVpcIPv4CIDR());
        response.setSubnetName(openStackVmInfo.getSubnetName());
        response.setSubnetIPv4Cidr(openStackVmInfo.getSubnetIPv4CIDR());
        response.setSecurityGroupName(openStackVmInfo.getSecurityGroupName());
        response.setKeypairName(openStackVmInfo.getKeypairName());
        response.setImageName(openStackVmInfo.getImageName());
        response.setVmSpec(openStackVmInfo.getVmSpec());

        List<SecurityGroupRuleDTO> ruleDTOs = openStackVmInfo.getSecurityGroupRules().stream()
            .map(this::convertToSecurityGroupRuleDTO)
            .collect(Collectors.toList());
        response.setSecurityGroupRules(ruleDTOs);

        return response;
    }

    private SecurityGroupRuleDTO convertToSecurityGroupRuleDTO(SecurityGroupRule rule) {
        SecurityGroupRuleDTO dto = new SecurityGroupRuleDTO();
        dto.setFromPort(rule.getFromPort());
        dto.setToPort(rule.getToPort());
        dto.setIpProtocol(rule.getIpProtocol());
        dto.setDirection(rule.getDirection());
        return dto;
    }

    private CreateVPCRequestDTO prepareVPCRequest(OpenStackVmInfo vmInfo) {
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

    private CreateSecurityGroupRequestDTO prepareSecurityGroupRequest(OpenStackVmInfo vmInfo) {
        CreateSecurityGroupRequestDTO.ReqInfo reqInfo = new CreateSecurityGroupRequestDTO.ReqInfo();
        reqInfo.setName(vmInfo.getSecurityGroupName());
        reqInfo.setVpcName(vmInfo.getVpcName());

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

    private CreateKeyPairRequestDTO prepareKeyPairRequest(OpenStackVmInfo vmInfo) {
        CreateKeyPairRequestDTO.ReqInfo reqInfo = new CreateKeyPairRequestDTO.ReqInfo();
        reqInfo.setName(vmInfo.getKeypairName());

        CreateKeyPairRequestDTO createKeyPairRequestDTO = new CreateKeyPairRequestDTO();
        createKeyPairRequestDTO.setConnectionName(vmInfo.getConnectionName());
        createKeyPairRequestDTO.setReqInfo(reqInfo);

        return createKeyPairRequestDTO;
    }

    private CreateVMRequestDTO prepareVMRequest(OpenStackVmInfo vmInfo) {
        CreateVMRequestDTO.ReqInfo reqInfo = new CreateVMRequestDTO.ReqInfo();
        reqInfo.setName(vmInfo.getVmName());
        reqInfo.setImageName(vmInfo.getImageName());
        reqInfo.setVpcName(vmInfo.getVpcName());
        reqInfo.setSubnetName(vmInfo.getSubnetName());
        reqInfo.setSecurityGroupNames(List.of(vmInfo.getSecurityGroupName()));
        reqInfo.setVmSpecName(vmInfo.getVmSpec());
        reqInfo.setKeyPairName(vmInfo.getKeypairName());

        CreateVMRequestDTO createVMRequestDTO = new CreateVMRequestDTO();
        createVMRequestDTO.setConnectionName(vmInfo.getConnectionName());
        createVMRequestDTO.setReqInfo(reqInfo);

        return createVMRequestDTO;
    }
}
