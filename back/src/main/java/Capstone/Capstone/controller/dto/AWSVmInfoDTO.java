package Capstone.Capstone.controller.dto;

import java.util.List;

public class AWSVmInfoDTO {

    private String userId;

    private String connectionName;

    private String vmName;

    private String vpcName;

    private String vpcIPv4Cidr;

    private String subnetName;

    private String subnetIPv4Cidr;

    private String securityGroupName;

    private List<SecurityGroupRuleDTO> securityGroupRules;

    private String keypairName;

    private String imageName;

    private String vmSpec;

    private String regionName;

    private String zoneName;

}
