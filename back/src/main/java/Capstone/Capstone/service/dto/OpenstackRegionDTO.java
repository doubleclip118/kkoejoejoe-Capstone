package Capstone.Capstone.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class OpenstackRegionDTO {
    @JsonProperty("RegionName")
    private String regionName;

    @JsonProperty("ProviderName")
    private String providerName;

    @JsonProperty("KeyValueInfoList")
    private List<KeyValueInfo> keyValueInfoList;
}
