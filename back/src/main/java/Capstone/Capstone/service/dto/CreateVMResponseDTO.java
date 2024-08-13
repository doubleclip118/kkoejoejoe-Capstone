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
public class CreateVMResponseDTO {

    @JsonProperty("IId")
    private IId iId;

    @JsonProperty("StartTime")
    private String startTime;

    @JsonProperty("ImageType")
    private String imageType;

    @JsonProperty("ImageIId")
    private IId imageIId;

    @JsonProperty("VMSpecName")
    private String vmSpecName;

    @JsonProperty("VpcIID")
    private IId vpcIId;

    @JsonProperty("SubnetIID")
    private IId subnetIId;

    @JsonProperty("SecurityGroupIIds")
    private List<IId> securityGroupIIds;

    @JsonProperty("KeyPairIId")
    private IId keyPairIId;

    @JsonProperty("RootDiskType")
    private String rootDiskType;

    @JsonProperty("RootDiskSize")
    private String rootDiskSize;

    @JsonProperty("RootDeviceName")
    private String rootDeviceName;

    @JsonProperty("DataDiskIIDs")
    private List<IId> dataDiskIIds;

    @JsonProperty("VMBootDisk")
    private String vmBootDisk;

    @JsonProperty("VMBlockDisk")
    private String vmBlockDisk;

    @JsonProperty("VMUserId")
    private String vmUserId;

    @JsonProperty("VMUserPasswd")
    private String vmUserPasswd;

    @JsonProperty("NetworkInterface")
    private String networkInterface;

    @JsonProperty("PublicIP")
    private String publicIP;

    @JsonProperty("PublicDNS")
    private String publicDNS;

    @JsonProperty("PrivateIP")
    private String privateIP;

    @JsonProperty("PrivateDNS")
    private String privateDNS;

    @JsonProperty("Platform")
    private String platform;

    @JsonProperty("SSHAccessPoint")
    private String sshAccessPoint;

    @JsonProperty("AccessPoint")
    private String accessPoint;

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
