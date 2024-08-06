package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.SecurityGroupRuleDTO;
import Capstone.Capstone.controller.dto.VmInfoDTO;
import Capstone.Capstone.controller.dto.VmInfoResponse;
import Capstone.Capstone.domain.AzureVmInfo;
import Capstone.Capstone.domain.SecurityGroupRule;
import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.AzureVmInfoRepository;
import Capstone.Capstone.repository.SecurityGroupRuleRepository;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.utils.error.UserNotFoundException;
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

    public AzureVmInfoService(AzureVmInfoRepository azureVmInfoRepository,
        UserRepository userRepository,
        SecurityGroupRuleRepository securityGroupRuleRepository) {
        this.azureVmInfoRepository = azureVmInfoRepository;
        this.userRepository = userRepository;
        this.securityGroupRuleRepository = securityGroupRuleRepository;
    }

    @Transactional
    public VmInfoResponse createAzureVmInfo(VmInfoDTO vmInfoDTO) {
        User user = userRepository.findById(vmInfoDTO.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        AzureVmInfo azureVmInfo = new AzureVmInfo(
            user,
            vmInfoDTO.getConnectionName(),
            vmInfoDTO.getVmName(),
            vmInfoDTO.getVpcName(),
            vmInfoDTO.getVpcIPv4Cidr(),
            vmInfoDTO.getSubnetName(),
            vmInfoDTO.getSubnetIPv4Cidr(),
            vmInfoDTO.getSecurityGroupName(),
            new ArrayList<>(),  // SecurityGroupRules will be added separately
            vmInfoDTO.getKeypairName(),
            vmInfoDTO.getImageName(),
            vmInfoDTO.getVmSpec(),
            vmInfoDTO.getRegionName(),
            vmInfoDTO.getZoneName(),
            null,
            null
        );

        AzureVmInfo savedAzureVmInfo = azureVmInfoRepository.save(azureVmInfo);

        for (SecurityGroupRuleDTO ruleDTO : vmInfoDTO.getSecurityGroupRules()) {
            SecurityGroupRule rule = new SecurityGroupRule(
                null,
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
        dto.setRegionName(azureVmInfo.getRegionName());
        dto.setZoneName(azureVmInfo.getZoneName());

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
}
