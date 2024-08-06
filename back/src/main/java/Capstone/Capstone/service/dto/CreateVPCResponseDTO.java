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
public class CreateVPCResponseDTO {

    @NotEmpty
    private IId IId;

    @NotEmpty
    private String IPv4_CIDR;

    @NotEmpty
    private List<SubnetInfo> SubnetInfoList;

    private List<Tag> TagList;
    private List<KeyValue> KeyValueList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IId {
        @NotEmpty
        private String NameId;

        @NotEmpty
        private String SystemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubnetInfo {
        @NotEmpty
        private IId IId;

        @NotEmpty
        private String Zone;

        @NotEmpty
        private String IPv4_CIDR;

        private List<Tag> TagList;
        private List<KeyValue> KeyValueList;
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
        @NotEmpty
        private String Key;

        @NotEmpty
        private String Value;
    }
}
