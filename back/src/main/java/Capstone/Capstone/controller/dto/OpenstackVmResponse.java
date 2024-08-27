package Capstone.Capstone.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenstackVmResponse {
    @NotEmpty
    private Long vmId;

    @NotEmpty
    private Long userId;

    @NotEmpty
    private String connectionName;

    @NotEmpty
    private String vmName;

    @NotEmpty
    private String imageType;

    @NotEmpty
    private String imageName;

    @NotEmpty
    private String vmSpecName;

    @NotEmpty
    private String vpcName;

    @NotEmpty
    private String vpcIPv4CIDR;

    @NotEmpty
    private String subnetName;

    @NotEmpty
    private String subnetIPv4CIDR;

    @NotEmpty
    private String securityGroupName;

    private List<SecurityGroupRuleDTO> securityGroupRules;

    @NotEmpty
    private String rootDiskType;

    private List<String> dataDiskNames;

    @NotEmpty
    private String keyPairName;

    @NotEmpty
    private String vmUserId;

    @NotEmpty
    private String vmUserPasswd;
}
