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
public class OpenstackVmCreateRequest {

    @NotEmpty
    @JsonProperty("ConnectionName")
    private String connectionName;

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

        @JsonProperty("ImageType")
        private String imageType;

        @JsonProperty("ImageName")
        private String imageName;

        @JsonProperty("VMSpecName")
        private String vmSpecName;

        @JsonProperty("VPCName")
        private String vpcName;

        @JsonProperty("SubnetName")
        private String subnetName;

        @JsonProperty("SecurityGroupNames")
        private List<String> securityGroupNames;

        @JsonProperty("RootDiskType")
        private String rootDiskType;

        @JsonProperty("DataDiskNames")
        private List<String> dataDiskNames;

        @JsonProperty("KeyPairName")
        private String keyPairName;

        @JsonProperty("VMUserId")
        private String vmUserId;

        @JsonProperty("VMUserPasswd")
        private String vmUserPasswd;
    }
}
