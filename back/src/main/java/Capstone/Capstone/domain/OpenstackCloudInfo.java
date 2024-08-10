package Capstone.Capstone.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenstackCloudInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "openstack_cloud_info", fetch = FetchType.LAZY)
    private User user;
    @Column
    private String driverName;
    @Column
    private String providerName;
    @Column
    private String driverLibFileName;
    @Column
    private String credentialName;
    @Column
    private String identityEndpointKey;
    @Column
    private String identityEndpointValue;
    @Column
    private String usernameKey;
    @Column
    private String usernameValue;
    @Column
    private String domainNameKey;
    @Column
    private String domainNameValue;
    @Column
    private String passwordKey;
    @Column
    private String passwordValue;
    @Column
    private String projectIDKey;
    @Column
    private String projectIDValue;
    @Column
    private String regionName;
    @Column
    private String regionKey;
    @Column
    private String regionValue;
    @Column
    private String configName;

}
