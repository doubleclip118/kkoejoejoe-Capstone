package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.*;
import Capstone.Capstone.controller.dto.OpenstackVmResponse;
import Capstone.Capstone.domain.OpenStackVmInfo;
import Capstone.Capstone.domain.SecurityGroupRule;
import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.OpenStackVmInfoRepository;
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
public class OpenstackVmInfoService {
    private final OpenStackVmInfoRepository openStackVmInfoRepository;
    private final UserRepository userRepository;
    private final SecurityGroupRuleRepository securityGroupRuleRepository;
    private final ExternalApiService externalApiService;

    public OpenstackVmInfoService(OpenStackVmInfoRepository openStackVmInfoRepository,
        UserRepository userRepository, SecurityGroupRuleRepository securityGroupRuleRepository,
        ExternalApiService externalApiService) {
        this.openStackVmInfoRepository = openStackVmInfoRepository;
        this.userRepository = userRepository;
        this.securityGroupRuleRepository = securityGroupRuleRepository;
        this.externalApiService = externalApiService;
    }

    @Transactional
    public OpenstackVmResponse createOpenStackVmInfo(OpenstackVmRequest vmInfoRequest) {
        User user = userRepository.findById(vmInfoRequest.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        OpenStackVmInfo openStackVmInfo = new OpenStackVmInfo(
            user,
            vmInfoRequest.getConnectionName(),
            vmInfoRequest.getVmName(),
            vmInfoRequest.getImageType(),
            vmInfoRequest.getImageName(),
            vmInfoRequest.getVmSpecName(),
            vmInfoRequest.getVpcName(),
            vmInfoRequest.getVpcIPv4CIDR(),
            vmInfoRequest.getSubnetName(),
            vmInfoRequest.getSubnetIPv4CIDR(),
            vmInfoRequest.getSecurityGroupName(),
            new ArrayList<>(),
            vmInfoRequest.getRootDiskType(),
            vmInfoRequest.getKeyPairName(),
            vmInfoRequest.getVmUserId(),
            vmInfoRequest.getVmUserPasswd()
        );

        OpenStackVmInfo savedOpenStackVmInfo = openStackVmInfoRepository.save(openStackVmInfo);

        for (SecurityGroupRuleDTO ruleDTO : vmInfoRequest.getSecurityGroupRules()) {
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

        return convertToOpenstackVmResponse(savedOpenStackVmInfo);
    }

    @Transactional
    public String deleteOpenStackVmInfo(Long id) {
        openStackVmInfoRepository.findById(id)
            .orElseThrow(() -> new VmInfoNotFoundException("VM Info Not Found"));
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

        vmInfo.setSecretkey(Base64.getEncoder().encodeToString(keyPairResponse.getPrivateKey().getBytes()));

        OpenstackVmCreateRequest createVMRequestDTO = prepareVMRequest(vmInfo);
        OpenstackVmCreateResponse vmResponse = externalApiService.createOpenstackVM(createVMRequestDTO);

        vmInfo.setIp(vmResponse.getPublicIP());
        openStackVmInfoRepository.save(vmInfo);

        return new VmcreateResponse(vmInfo.getUserInfo().getId(), vmInfo.getId(), vmInfo.getSecretkey(), vmInfo.getIp());
    }

    @Transactional
    public String deleteVm(Long vmid) {
        OpenStackVmInfo vmInfo = openStackVmInfoRepository.findById(vmid)
            .orElseThrow(() -> new VmInfoNotFoundException("Vm info Not Found"));
        externalApiService.deleteVm(vmInfo.getVmName(), vmInfo.getConnectionName());
        openStackVmInfoRepository.deleteById(vmid);
        return "삭제 완료";
    }

    public List<GetVmDTO> getVmDTOList(Long id) {
        User user = userRepository.findByUserIdWithVAndOpenstackVmInfos(id)
            .orElseThrow(() -> new UserNotFoundException("User with vm not found"));
        return user.getOpenStackVmInfos().stream()
            .map(openStackVmInfo -> new GetVmDTO(openStackVmInfo.getId(), openStackVmInfo.getVmName(),openStackVmInfo.getIp(),new String(Base64.getDecoder().decode(openStackVmInfo.getSecretkey()))))
            .collect(Collectors.toList());
    }

    private OpenstackVmResponse convertToOpenstackVmResponse(OpenStackVmInfo openStackVmInfo) {
        OpenstackVmResponse response = new OpenstackVmResponse();
        response.setVmId(openStackVmInfo.getId());
        response.setUserId(openStackVmInfo.getUserInfo().getId());
        response.setConnectionName(openStackVmInfo.getConnectionName());
        response.setVmName(openStackVmInfo.getVmName());
        response.setImageType(openStackVmInfo.getImageType());
        response.setImageName(openStackVmInfo.getImageName());
        response.setVmSpecName(openStackVmInfo.getVmSpecName());
        response.setVpcName(openStackVmInfo.getVpcName());
        response.setVpcIPv4CIDR(openStackVmInfo.getVpcIPv4CIDR());
        response.setSubnetName(openStackVmInfo.getSubnetName());
        response.setSubnetIPv4CIDR(openStackVmInfo.getSubnetIPv4CIDR());
        response.setSecurityGroupName(openStackVmInfo.getSecurityGroupName());
        response.setRootDiskType(openStackVmInfo.getRootDiskType());
        response.setKeyPairName(openStackVmInfo.getKeyPairName());
        response.setVmUserId(openStackVmInfo.getVmUserId());
        response.setVmUserPasswd(openStackVmInfo.getVmUserPasswd());

        List<SecurityGroupRuleDTO> ruleDTOs = openStackVmInfo.getSecurityGroupRules().stream()
            .map(this::convertToSecurityGroupRuleDTO)
            .collect(Collectors.toList());
        response.setSecurityGroupRules(ruleDTOs);

        // Note: dataDiskNames is not present in the OpenStackVmInfo entity
        // You may need to add this field to the entity or fetch it from another source
        response.setDataDiskNames(new ArrayList<>());

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
        reqInfo.setName(vmInfo.getKeyPairName());

        CreateKeyPairRequestDTO createKeyPairRequestDTO = new CreateKeyPairRequestDTO();
        createKeyPairRequestDTO.setConnectionName(vmInfo.getConnectionName());
        createKeyPairRequestDTO.setReqInfo(reqInfo);

        return createKeyPairRequestDTO;
    }

    private OpenstackVmCreateRequest prepareVMRequest(OpenStackVmInfo vmInfo) {
        OpenstackVmCreateRequest.ReqInfo reqInfo = new OpenstackVmCreateRequest.ReqInfo();
        reqInfo.setName(vmInfo.getVmName());
        reqInfo.setImageType(vmInfo.getImageType());
        reqInfo.setImageName(vmInfo.getImageName());
        reqInfo.setVmSpecName(vmInfo.getVmSpecName());
        reqInfo.setVpcName(vmInfo.getVpcName());
        reqInfo.setSubnetName(vmInfo.getSubnetName());
        reqInfo.setSecurityGroupNames(List.of(vmInfo.getSecurityGroupName()));
        reqInfo.setRootDiskType(vmInfo.getRootDiskType());
        reqInfo.setDataDiskNames(new ArrayList<>());
        reqInfo.setKeyPairName(vmInfo.getKeyPairName());
        reqInfo.setVmUserId(vmInfo.getVmUserId());
        reqInfo.setVmUserPasswd(vmInfo.getVmUserPasswd());

        OpenstackVmCreateRequest createVMRequestDTO = new OpenstackVmCreateRequest();
        createVMRequestDTO.setConnectionName(vmInfo.getConnectionName());
        createVMRequestDTO.setReqInfo(reqInfo);

        return createVMRequestDTO;
    }
}
