package Capstone.Capstone.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSecurityGroupRequestDTO {

    @NotEmpty
    @JsonProperty("ConnectionName")
    private String connectionName;

    @NotEmpty
    @JsonProperty("ReqInfo")
    private ReqInfo reqInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqInfo {
        @NotEmpty
        @JsonProperty("Name")
        private String name;

        @NotEmpty
        @JsonProperty("VPCName")
        private String vpcName;

        @NotEmpty
        @JsonProperty("SecurityRules")
        private List<SecurityRule> securityRules;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SecurityRule {
        @NotEmpty
        @JsonProperty("FromPort")
        private String fromPort;

        @NotEmpty
        @JsonProperty("ToPort")
        private String toPort;

        @NotEmpty
        @JsonProperty("IPProtocol")
        private String ipProtocol;

        @NotEmpty
        @JsonProperty("Direction")
        private String direction;
    }
}
