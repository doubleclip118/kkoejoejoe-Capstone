package Capstone.Capstone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "security_group_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityGroupRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vm_aws_configuration_id")
    private AWSVmInfo awsVmInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vm_azure_configuration_id")
    private AzureVmInfo azureVmInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vm_openstack_configuration_id")
    private OpenStackVmInfo openstackVmInfo;

    @Column(name = "from_port")
    private String fromPort;

    @Column(name = "to_port")
    private String toPort;

    @Column(name = "ip_protocol")
    private String ipProtocol;

    @Column(name = "direction")
    private String direction;

    // AWS VM Info를 위한 생성자
    public SecurityGroupRule(AWSVmInfo awsVmInfo, String fromPort, String toPort, String ipProtocol, String direction) {
        this.awsVmInfo = awsVmInfo;
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.ipProtocol = ipProtocol;
        this.direction = direction;
    }

    // Azure VM Info를 위한 생성자
    public SecurityGroupRule(AzureVmInfo azureVmInfo, String fromPort, String toPort, String ipProtocol, String direction) {
        this.azureVmInfo = azureVmInfo;
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.ipProtocol = ipProtocol;
        this.direction = direction;
    }

    // OpenStack VM Info를 위한 생성자
    public SecurityGroupRule(OpenStackVmInfo openstackVmInfo, String fromPort, String toPort, String ipProtocol, String direction) {
        this.openstackVmInfo = openstackVmInfo;
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.ipProtocol = ipProtocol;
        this.direction = direction;
    }

    public void setAwsVmInfo(AWSVmInfo awsVmInfo) {
        this.awsVmInfo = awsVmInfo;
        if (awsVmInfo != null && !awsVmInfo.getSecurityGroupRules().contains(this)) {
            awsVmInfo.getSecurityGroupRules().add(this);
        }
    }

    public void setAzureVmInfo(AzureVmInfo azureVmInfo) {
        this.azureVmInfo = azureVmInfo;
        if (azureVmInfo != null && !azureVmInfo.getSecurityGroupRules().contains(this)) {
            azureVmInfo.getSecurityGroupRules().add(this);
        }
    }

    public void setOpenstackVmInfo(OpenStackVmInfo openstackVmInfo) {
        this.openstackVmInfo = openstackVmInfo;
        if (openstackVmInfo != null && !openstackVmInfo.getSecurityGroupRules().contains(this)) {
            openstackVmInfo.getSecurityGroupRules().add(this);
        }
    }
}
