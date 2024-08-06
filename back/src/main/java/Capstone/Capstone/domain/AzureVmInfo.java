package Capstone.Capstone.domain;

import Capstone.Capstone.domain.AWSVmInfo.SecurityGroupRule;
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
    private String subnetIPv4_CIDR;

    @Column(name = "security_group_name")
    private String securityGroupName;

    @ElementCollection
    @CollectionTable(name = "security_group_rules", joinColumns = @JoinColumn(name = "vm_configuration_id"))
    private List<AWSVmInfo.SecurityGroupRule> securityGroupRules = new ArrayList<>();

    @Column(name = "keypair_name")
    private String keypairName;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "vm_spec")
    private String vmSpec;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "zone_name")
    private String zoneName;

    @Column(name = "secret_key")
    private String secretkey;

    @Column(name = "ip")
    private String ip;


    @Embeddable
    public static class SecurityGroupRule {
        @Column(name = "from_port")
        private String fromPort;

        @Column(name = "to_port")
        private String toPort;

        @Column(name = "ip_protocol")
        private String ipProtocol;

        @Column(name = "direction")
        private String direction;
    }

}
