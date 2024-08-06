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
public class CreateVPCResponseDTO {

    @JsonProperty("IId")
    @NotEmpty
    private IId iId;

    @JsonProperty("IPv4_CIDR")
    @NotEmpty
    private String ipv4Cidr;

    @JsonProperty("SubnetInfoList")
    @NotEmpty
    private List<SubnetInfo> subnetInfoList;

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
    public static class SubnetInfo {
        @JsonProperty("IId")
        private IId iId;

        @JsonProperty("Zone")
        private String zone;

        @JsonProperty("IPv4_CIDR")
        private String ipv4Cidr;

        @JsonProperty("TagList")
        private List<Tag> tagList;

        @JsonProperty("KeyValueList")
        private List<KeyValue> keyValueList;
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
