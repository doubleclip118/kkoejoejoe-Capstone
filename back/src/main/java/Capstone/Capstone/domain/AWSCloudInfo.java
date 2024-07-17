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
public class AWSCloudInfo {
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
    private String CredentialAccessKey;
    @Column
    private String CredentialAccessKeyVal;
    @Column
    private String CredentialSecretKey;
    @Column
    private String CredentialSecretKeyVal;
    @Column
    private String RegionName;
    @Column
    private String RegionKey;
    @Column
    private String RegionValue;
    @Column
    private String ZoneKey;
    @Column
    private String ZoneValue;
}
