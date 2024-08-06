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
public class CreateSecurityGroupResponseDTO {

    private IId IId;

    private VpcIID VpcIID;

    private List<SecurityRule> SecurityRules;

    private List<Tag> TagList;
    private List<KeyValue> KeyValueList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IId {
        private String NameId;
        private String SystemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VpcIID {
        private String NameId;
        private String SystemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SecurityRule {
        private String Direction;

        private String IPProtocol;

        private String FromPort;
        private String ToPort;
        private String CIDR;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        private String Key;
        private String Value;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyValue {
        private String Key;
        private String Value;
    }
}
