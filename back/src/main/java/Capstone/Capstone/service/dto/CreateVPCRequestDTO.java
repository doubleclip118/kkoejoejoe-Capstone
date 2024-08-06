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
public class CreateVPCRequestDTO {
    @NotEmpty
    @JsonProperty("ConnectionName")
    private String ConnectionName;

    @NotEmpty
    @JsonProperty("ReqInfo")
    private ReqInfo ReqInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqInfo {
        @NotEmpty
        @JsonProperty("Name")
        private String Name;

        @NotEmpty
        @JsonProperty("IPv4_CIDR")
        private String IPv4CIDR;

        @JsonProperty("SubnetInfoList")
        private List<SubnetInfo> SubnetInfoList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubnetInfo {
        @NotEmpty
        @JsonProperty("Name")
        private String Name;

        @NotEmpty
        @JsonProperty("IPv4_CIDR")
        private String IPv4CIDR;
    }

}
