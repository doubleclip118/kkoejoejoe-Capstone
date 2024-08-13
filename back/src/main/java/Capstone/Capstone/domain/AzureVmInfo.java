package Capstone.Capstone.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class AzureVmInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userInfo;

    @Column(name = "connection_name")
    private String connectionName;

    @Column(name = "vm_name")
    private String vmName;

    @Column(name = "vpc_name")
    private String vpcName;

    @Column(name = "vpc_ipv4_cidr")
    private String vpcIPv4CIDR;

    @Column(name = "subnet_name")
    private String subnetName;

    @Column(name = "subnet_ipv4_cidr")
    private String subnetIPv4CIDR;

    @Column(name = "security_group_name")
    private String securityGroupName;

    @OneToMany(mappedBy = "azureVmInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SecurityGroupRule> securityGroupRules = new ArrayList<>();

    @Column(name = "keypair_name")
    private String keypairName;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "vm_spec")
    private String vmSpec;

    @Column(name = "secret_key", length = 5000)
    private String secretkey;

    @Column(name = "ip")
    private String ip;


    public AzureVmInfo(User userInfo, String connectionName, String vmName, String vpcName,
        String vpcIPv4CIDR, String subnetName, String subnetIPv4CIDR, String securityGroupName,
        List<SecurityGroupRule> securityGroupRules, String keypairName, String imageName,
        String vmSpec,
        String secretkey, String ip) {
        this.userInfo = userInfo;
        this.connectionName = connectionName;
        this.vmName = vmName;
        this.vpcName = vpcName;
        this.vpcIPv4CIDR = vpcIPv4CIDR;
        this.subnetName = subnetName;
        this.subnetIPv4CIDR = subnetIPv4CIDR;
        this.securityGroupName = securityGroupName;
        this.securityGroupRules = securityGroupRules;
        this.keypairName = keypairName;
        this.imageName = imageName;
        this.vmSpec = vmSpec;
        this.secretkey = secretkey;
        this.ip = ip;
    }

    public void addSecurityGroupRule(SecurityGroupRule rule) {
        securityGroupRules.add(rule);
        rule.setAzureVmInfo(this);
    }
}
