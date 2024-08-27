package Capstone.Capstone.domain;

import Capstone.Capstone.controller.dto.SecurityGroupRuleDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenStackVmInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userInfo;
    @Column
    private String connectionName;

    @Column
    private String vmName;

    @Column
    private String imageType;

    @Column
    private String imageName;

    @Column
    private String vmSpecName;

    @Column
    private String vpcName;

    @Column
    private String vpcIPv4CIDR;

    @Column
    private String subnetName;

    @Column
    private String subnetIPv4CIDR;

    @Column
    private String securityGroupName;

    @OneToMany(mappedBy = "openstackVmInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SecurityGroupRule> securityGroupRules = new ArrayList<>();

    @Column
    private String rootDiskType;


    @Column
    private String keyPairName;

    @Column
    private String vmUserId;

    @Column
    private String vmUserPasswd;

    @Column(name = "secret_key", length = 5000)
    private String secretkey;

    @Column(name = "ip")
    private String ip;

    public OpenStackVmInfo(User userInfo, String connectionName, String vmName, String imageType,
        String imageName, String vmSpecName, String vpcName, String vpcIPv4CIDR, String subnetName,
        String subnetIPv4CIDR, String securityGroupName, List<SecurityGroupRule> securityGroupRules,
        String rootDiskType, String keyPairName, String vmUserId, String vmUserPasswd,
        String secretkey,
        String ip) {
        this.userInfo = userInfo;
        this.connectionName = connectionName;
        this.vmName = vmName;
        this.imageType = imageType;
        this.imageName = imageName;
        this.vmSpecName = vmSpecName;
        this.vpcName = vpcName;
        this.vpcIPv4CIDR = vpcIPv4CIDR;
        this.subnetName = subnetName;
        this.subnetIPv4CIDR = subnetIPv4CIDR;
        this.securityGroupName = securityGroupName;
        this.securityGroupRules = securityGroupRules;
        this.rootDiskType = rootDiskType;
        this.keyPairName = keyPairName;
        this.vmUserId = vmUserId;
        this.vmUserPasswd = vmUserPasswd;
        this.secretkey = secretkey;
        this.ip = ip;
    }

    public OpenStackVmInfo(User userInfo, String connectionName, String vmName, String imageType,
        String imageName, String vmSpecName, String vpcName, String vpcIPv4CIDR, String subnetName,
        String subnetIPv4CIDR, String securityGroupName, List<SecurityGroupRule> securityGroupRules,
        String rootDiskType, String keyPairName, String vmUserId, String vmUserPasswd) {
        this.userInfo = userInfo;
        this.connectionName = connectionName;
        this.vmName = vmName;
        this.imageType = imageType;
        this.imageName = imageName;
        this.vmSpecName = vmSpecName;
        this.vpcName = vpcName;
        this.vpcIPv4CIDR = vpcIPv4CIDR;
        this.subnetName = subnetName;
        this.subnetIPv4CIDR = subnetIPv4CIDR;
        this.securityGroupName = securityGroupName;
        this.securityGroupRules = securityGroupRules;
        this.rootDiskType = rootDiskType;
        this.keyPairName = keyPairName;
        this.vmUserId = vmUserId;
        this.vmUserPasswd = vmUserPasswd;
    }

    public void addSecurityGroupRule(SecurityGroupRule rule) {
        securityGroupRules.add(rule);
        rule.setOpenstackVmInfo(this);
    }
}
