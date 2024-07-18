package Capstone.Capstone.controller.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AWSInfoRequest {
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
    private String credentialAccessKey;
    @NotEmpty
    private String credentialAccessKeyVal;
    @NotEmpty
    private String credentialSecretKey;
    @NotEmpty
    private String credentialSecretKeyVal;
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
