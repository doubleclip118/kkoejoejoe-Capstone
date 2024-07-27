package Capstone.Capstone.controller.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AzureInfoRequest {
    @NotNull
    private Long userId;
    @NotEmpty
    private String driverName;
    @NotEmpty
    private String providerName;
    @NotEmpty
    private String driverLibFileName;
    @NotEmpty
    private String credentialName;
    @NotEmpty
    private String clientIdKey;
    @NotEmpty
    private String clientIdValue;
    @NotEmpty
    private String clientSecretKey;
    @NotEmpty
    private String clientSecretValue;
    @NotEmpty
    private String tenantIdKey;
    @NotEmpty
    private String tenantIdValue;
    @NotEmpty
    private String subscriptionIdKey;
    @NotEmpty
    private String subscriptionIdValue;
    @NotEmpty
    private String regionName;
    @NotEmpty
    private String regionKey;
    @NotEmpty
    private String regionValue;
    @NotEmpty
    private String zoneKey;
    @NotEmpty
    private String zoneValue;
    @NotEmpty
    private String configName;
}
