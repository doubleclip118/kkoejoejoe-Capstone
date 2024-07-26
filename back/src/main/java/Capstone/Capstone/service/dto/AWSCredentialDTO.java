package Capstone.Capstone.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AWSCredentialDTO {
    @JsonProperty("CredentialName")
    private String credentialName;

    @JsonProperty("ProviderName")
    private String providerName;

    @JsonProperty("KeyValueInfoList")
    private List<KeyValueInfo> keyValueInfoList;
}

