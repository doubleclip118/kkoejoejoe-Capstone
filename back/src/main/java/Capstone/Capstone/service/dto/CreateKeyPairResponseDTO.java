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
public class CreateKeyPairResponseDTO {

    @JsonProperty("IId")
    private IId iId;

    @JsonProperty("Fingerprint")
    private String fingerprint;

    @JsonProperty("PublicKey")
    private String publicKey;

    @JsonProperty("PrivateKey")
    private String privateKey;

    @JsonProperty("VMUserID")
    private String vmUserId;

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
