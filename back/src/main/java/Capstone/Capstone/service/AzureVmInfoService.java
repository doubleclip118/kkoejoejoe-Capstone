package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.SecurityGroupRuleDTO;
import Capstone.Capstone.controller.dto.VmInfoDTO;
import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.domain.AWSVmInfo.SecurityGroupRule;
import Capstone.Capstone.domain.AzureVmInfo;
import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.AWSVmInfoRepository;
import Capstone.Capstone.repository.AzureCloudInfoRepository;
import Capstone.Capstone.repository.AzureVmInfoRepository;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.utils.error.UserNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AzureVmInfoService {
    private final AzureVmInfoRepository azureVmInfoRepository;
    private final UserRepository userRepository;

    public AzureVmInfoService(AzureVmInfoRepository azureVmInfoRepository,
        UserRepository userRepository) {
        this.azureVmInfoRepository = azureVmInfoRepository;
        this.userRepository = userRepository;
    }



    public VmInfoDTO createAWSVmInfo(VmInfoDTO vmInfoDTO) {
        User user = userRepository.findById(vmInfoDTO.getUserId()).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );

        List<SecurityGroupRule> securityGroupRules = vmInfoDTO.getSecurityGroupRules().stream()
            .map(this::convertToSecurityGroupRule)
            .collect(Collectors.toList());

        AzureVmInfo awsVmInfo = new AzureVmInfo(
            user,
            vmInfoDTO.getConnectionName(),
            vmInfoDTO.getVmName(),
            vmInfoDTO.getVpcName(),
            vmInfoDTO.getVpcIPv4Cidr(),
            vmInfoDTO.getSubnetName(),
            vmInfoDTO.getSubnetIPv4Cidr(),
            vmInfoDTO.getSecurityGroupName(),
            securityGroupRules,
            vmInfoDTO.getKeypairName(),
            vmInfoDTO.getImageName(),
            vmInfoDTO.getVmSpec(),
            vmInfoDTO.getRegionName(),
            vmInfoDTO.getZoneName(),
            null,  // secretkey는 DTO에 없으므로 null로 설정
            null   // ip도 DTO에 없으므로 null로 설정
        );

        AzureVmInfo save = azureVmInfoRepository.save(awsVmInfo);

        // 저장된 엔티티를 다시 DTO로 변환하여 반환
        return convertToVmInfoDTO(save);
    }
    public String deleteAWSVmInfo(Long id){
        azureVmInfoRepository.deleteById(id);

        return "삭제 완료";
    }

    private SecurityGroupRule convertToSecurityGroupRule(SecurityGroupRuleDTO dto) {
        SecurityGroupRule rule = new SecurityGroupRule();
        rule.setFromPort(dto.getFromPort());
        rule.setToPort(dto.getToPort());
        rule.setIpProtocol(dto.getIpProtocol());
        rule.setDirection(dto.getDirection());
        return rule;
    }

    private VmInfoDTO convertToVmInfoDTO(AzureVmInfo awsVmInfo) {
        VmInfoDTO dto = new VmInfoDTO();
        dto.setUserId(awsVmInfo.getUserInfo().getId());
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
        dto.setRegionName(awsVmInfo.getRegionName());
        dto.setZoneName(awsVmInfo.getZoneName());

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


}
