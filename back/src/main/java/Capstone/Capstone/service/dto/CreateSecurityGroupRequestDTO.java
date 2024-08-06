package Capstone.Capstone.service.dto;

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
    private String connectionName;

    @NotEmpty
    private ReqInfo reqInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqInfo {
        @NotEmpty
        private String name;

        @NotEmpty
        private String vpcName;

        @NotEmpty
        private List<SecurityRule> securityRules;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SecurityRule {
        @NotEmpty
        private String fromPort;

        @NotEmpty
        private String toPort;

        @NotEmpty
        private String ipProtocol;

        @NotEmpty
        private String direction;
    }
}
