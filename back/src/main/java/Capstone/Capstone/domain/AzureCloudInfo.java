package Capstone.Capstone.domain;

import Capstone.Capstone.controller.dto.AzureInfoRequest;
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
public class AzureCloudInfo {
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
    private String SubscriptionIdKey;
    @Column
    private String SubscriptionIdValue;
    @Column
    private String TenantIdValue;
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

    public void updateAzureInfo(AzureInfoRequest azureInfoRequest){
        this.DriverName = azureInfoRequest.getDriverName();
        this.ProviderName = azureInfoRequest.getProviderName();
        this.DriverLibFileName = azureInfoRequest.getDriverLibFileName();
        this.CredentialName = azureInfoRequest.getCredentialName();
        this.ClientIdkey = azureInfoRequest.getClientIdKey();
        this.ClientIdValue = azureInfoRequest.getClientIdValue();
        this.ClientSecretKey = azureInfoRequest.getClientSecretKey();
        this.ClientSecretValue = azureInfoRequest.getClientSecretValue();
        this.TenantIdKey = azureInfoRequest.getTenantIdKey();
        this.TenantIdValue = azureInfoRequest.getTenantIdValue();
        this.RegionName = azureInfoRequest.getRegionName();
        this.RegionKey = azureInfoRequest.getRegionKey();
        this.RegionValue = azureInfoRequest.getRegionValue();
        this.ZoneKey = azureInfoRequest.getZoneKey();
        this.ZoneValue = azureInfoRequest.getZoneValue();
        this.ConfigName = azureInfoRequest.getConfigName();
        this.SubscriptionIdKey = azureInfoRequest.getSubscriptionIdKey();
        this.SubscriptionIdValue = azureInfoRequest.getSubscriptionIdValue();
    }
}
