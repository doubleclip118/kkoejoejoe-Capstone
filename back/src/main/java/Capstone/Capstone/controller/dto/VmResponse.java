package Capstone.Capstone.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VmResponse {
    @NotEmpty
    private Long userId;
    @NotEmpty
    private String connectionName;
    @NotEmpty
    private String vmName;
    @NotEmpty
    private String vpcName;
    @NotEmpty
    private String vpcIPv4Cidr;
    @NotEmpty
    private String subnetName;
    @NotEmpty
    private String subnetIPv4Cidr;
    @NotEmpty
    private String securityGroupName;

    private List<SecurityGroupRuleDTO> securityGroupRules;
    @NotEmpty
    private String keypairName;
    @NotEmpty
    private String imageName;
    @NotEmpty
    private String vmSpec;
    @NotEmpty
    private String privateKey;
    @NotEmpty
    private String ip;

}
