package Capstone.Capstone.controller.dto;

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
public class OpenStackInfoRequest {
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
    private String identityEndpointKey;

    @NotEmpty
    private String identityEndpointValue;

    @NotEmpty
    private String usernameKey;

    @NotEmpty
    private String usernameValue;

    @NotEmpty
    private String domainNameKey;

    @NotEmpty
    private String domainNameValue;

    @NotEmpty
    private String passwordKey;

    @NotEmpty
    private String passwordValue;

    @NotEmpty
    private String projectIDKey;

    @NotEmpty
    private String projectIDValue;

    @NotEmpty
    private String regionName;

    @NotEmpty
    private String regionKey;

    @NotEmpty
    private String regionValue;

    @NotEmpty
    private String configName;
}
