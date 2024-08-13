package Capstone.Capstone.domain;

import Capstone.Capstone.controller.dto.AWSInfoRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class AWSCloudInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
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
    @Column
    private String ConfigName;

    public void updateAWSInfo(AWSInfoRequest awsInfoRequest){
        this.DriverName = awsInfoRequest.getDriverName();
        this.ProviderName = awsInfoRequest.getProviderName();
        this.DriverLibFileName = awsInfoRequest.getDriverLibFileName();
        this.CredentialName = awsInfoRequest.getCredentialName();
        this.CredentialAccessKey = awsInfoRequest.getCredentialAccessKey();
        this.CredentialAccessKeyVal = awsInfoRequest.getCredentialAccessKeyVal();
        this.CredentialSecretKey = awsInfoRequest.getCredentialSecretKey();
        this.CredentialSecretKeyVal = awsInfoRequest.getCredentialSecretKeyVal();
        this.RegionName = awsInfoRequest.getRegionName();
        this.RegionKey = awsInfoRequest.getRegionKey();
        this.RegionValue = awsInfoRequest.getRegionValue();
        this.ZoneKey = awsInfoRequest.getZoneKey();
        this.ZoneValue = awsInfoRequest.getZoneValue();
        this.ConfigName = awsInfoRequest.getConfigName();
    }
}
