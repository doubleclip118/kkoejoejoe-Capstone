package Capstone.Capstone.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AzureCloudInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String DriverName;
    @Column
    private String ProviderName;
    @Column
    private String DriverLibFileName;
    @Column
    private String CredentialName;
    @Column
    private String ClientIdkey;
    @Column
    private String ClientIdValue;
    @Column
    private String ClientSecretKey;
    @Column
    private String ClientSecretValue;
    @Column
    private String TenantIdKey;
    @Column
    private String TenantIdValue;
    @Column
    private String RegionName;
    @Column
    private String RegionKey;
    @Column
    private String RigionValue;
    @Column
    private String ZoneKey;
    @Column
    private String ZoneValue;

}
