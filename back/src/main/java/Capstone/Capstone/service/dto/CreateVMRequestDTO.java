package Capstone.Capstone.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateVMRequestDTO {

    @JsonProperty("ConnectionName")
    private String connectionName;

    @JsonProperty("ReqInfo")
    private ReqInfo reqInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqInfo {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("ImageName")
        private String imageName;

        @JsonProperty("VPCName")
        private String vpcName;

        @JsonProperty("SubnetName")
        private String subnetName;

        @JsonProperty("SecurityGroupNames")
        private List<String> securityGroupNames;

        @JsonProperty("VMSpecName")
        private String vmSpecName;

        @JsonProperty("KeyPairName")
        private String keyPairName;
    }
}
