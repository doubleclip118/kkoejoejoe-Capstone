package Capstone.Capstone.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AzureInfoRequest {
    @NotEmpty
    private String DriverName;
    @NotEmpty
    private String ProviderName;
    @NotEmpty
    private String DriverLibFileName;
    @NotEmpty
    private String CredentialName;
    @NotEmpty
    private String ClientIdkey;
    @NotEmpty
    private String ClientIdValue;
    @NotEmpty
    private String ClientSecretKey;
    @NotEmpty
    private String ClientSecretValue;
    @NotEmpty
    private String TenantIdKey;
    @NotEmpty
    private String TenantIdValue;
    @NotEmpty
    private String RegionName;
    @NotEmpty
    private String RegionKey;
    @NotEmpty
    private String RigionValue;
    @NotEmpty
    private String ZoneKey;
    @NotEmpty
    private String ZoneValue;
}
