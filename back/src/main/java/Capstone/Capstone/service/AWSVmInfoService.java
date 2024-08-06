package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.SecurityGroupRuleDTO;
import Capstone.Capstone.controller.dto.VmInfoDTO;
import Capstone.Capstone.controller.dto.VmResponse;
import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.domain.AWSVmInfo.SecurityGroupRule;
import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.AWSVmInfoRepository;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.utils.error.UserNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AWSVmInfoService {
    private final AWSVmInfoRepository awsVmInfoRepository;
    private final UserRepository userRepository;

    public AWSVmInfoService(AWSVmInfoRepository awsVmInfoRepository,
        UserRepository userRepository) {
        this.awsVmInfoRepository = awsVmInfoRepository;
        this.userRepository = userRepository;
    }

    public VmInfoDTO createAWSVmInfo(VmInfoDTO vmInfoDTO) {
        User user = userRepository.findById(vmInfoDTO.getUserId()).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );

        List<SecurityGroupRule> securityGroupRules = vmInfoDTO.getSecurityGroupRules().stream()
            .map(this::convertToSecurityGroupRule)
            .collect(Collectors.toList());

        AWSVmInfo awsVmInfo = new AWSVmInfo(
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


        AWSVmInfo savedAWSVmInfo = awsVmInfoRepository.save(awsVmInfo);

        // 저장된 엔티티를 다시 DTO로 변환하여 반환
        return convertToVmInfoDTO(savedAWSVmInfo);
    }

    public String deleteAWSVmInfo(Long id){
        awsVmInfoRepository.deleteById(id);

        return "삭제 완료";
    }

    public VmResponse createVm(Long id){
        awsVmInfoRepository.findById(id).orElseThrow(
            () -> new
        );



    }

    private AWSVmInfo.SecurityGroupRule convertToSecurityGroupRule(SecurityGroupRuleDTO dto) {
        AWSVmInfo.SecurityGroupRule rule = new AWSVmInfo.SecurityGroupRule();
        rule.setFromPort(dto.getFromPort());
        rule.setToPort(dto.getToPort());
        rule.setIpProtocol(dto.getIpProtocol());
        rule.setDirection(dto.getDirection());
        return rule;
    }

    private VmInfoDTO convertToVmInfoDTO(AWSVmInfo awsVmInfo) {
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

    private SecurityGroupRuleDTO convertToSecurityGroupRuleDTO(AWSVmInfo.SecurityGroupRule rule) {
        SecurityGroupRuleDTO dto = new SecurityGroupRuleDTO();
        dto.setFromPort(rule.getFromPort());
        dto.setToPort(rule.getToPort());
        dto.setIpProtocol(rule.getIpProtocol());
        dto.setDirection(rule.getDirection());
        return dto;
    }




}
