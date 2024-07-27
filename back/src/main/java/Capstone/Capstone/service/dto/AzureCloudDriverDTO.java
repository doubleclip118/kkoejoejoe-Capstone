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
public class AzureCloudDriverDTO{
    @JsonProperty("DriverName")
    private String driverName;

    @JsonProperty("ProviderName")
    private String providerName;

    @JsonProperty("DriverLibFileName")
    private String driverLibFileName;
}