package Capstone.Capstone.service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenstackConfigDTO {

    @JsonProperty("ConfigName")
    private String configName;

    @JsonProperty("ProviderName")
    private String providerName;

    @JsonProperty("DriverName")
    private String driverName;

    @JsonProperty("CredentialName")
    private String credentialName;

    @JsonProperty("RegionName")
    private String regionName;

}
