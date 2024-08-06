package Capstone.Capstone.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSecurityGroupResponseDTO {

    @JsonProperty("IId")
    private IId iId;

    @JsonProperty("VpcIID")
    private VpcIID vpcIId;

    @JsonProperty("SecurityRules")
    private List<SecurityRule> securityRules;

    @JsonProperty("TagList")
    private List<Tag> tagList;

    @JsonProperty("KeyValueList")
    private List<KeyValue> keyValueList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IId {
        @JsonProperty("NameId")
        private String nameId;

        @JsonProperty("SystemId")
        private String systemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VpcIID {
        @JsonProperty("NameId")
        private String nameId;

        @JsonProperty("SystemId")
        private String systemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SecurityRule {
        @JsonProperty("Direction")
        private String direction;

        @JsonProperty("IPProtocol")
        private String ipProtocol;

        @JsonProperty("FromPort")
        private String fromPort;

        @JsonProperty("ToPort")
        private String toPort;

        @JsonProperty("CIDR")
        private String cidr;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        @JsonProperty("Key")
        private String key;

        @JsonProperty("Value")
        private String value;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyValue {
        @JsonProperty("Key")
        private String key;

        @JsonProperty("Value")
        private String value;
    }
}
