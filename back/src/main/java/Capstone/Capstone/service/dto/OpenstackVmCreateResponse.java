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
public class OpenstackVmCreateResponse {

    @JsonProperty("IId")
    private IId iId;

    @JsonProperty("StartTime")
    private String startTime;

    @JsonProperty("Region")
    private Region region;

    @JsonProperty("ImageType")
    private String imageType;

    @JsonProperty("ImageIId")
    private ImageIId imageIId;

    @JsonProperty("VMSpecName")
    private String vmSpecName;

    @JsonProperty("VpcIID")
    private VpcIID vpcIId;

    @JsonProperty("SubnetIID")
    private SubnetIID subnetIId;

    @JsonProperty("SecurityGroupIIds")
    private List<SecurityGroupIId> securityGroupIIds;

    @JsonProperty("KeyPairIId")
    private KeyPairIId keyPairIId;

    @JsonProperty("RootDiskType")
    private String rootDiskType;

    @JsonProperty("RootDiskSize")
    private String rootDiskSize;

    @JsonProperty("RootDeviceName")
    private String rootDeviceName;

    @JsonProperty("DataDiskIIDs")
    private List<DataDiskIId> dataDiskIIds;

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
    public static class Region {
        @JsonProperty("Region")
        private String region;

        @JsonProperty("Zone")
        private String zone;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageIId {
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
    public static class SubnetIID {
        @JsonProperty("NameId")
        private String nameId;

        @JsonProperty("SystemId")
        private String systemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SecurityGroupIId {
        @JsonProperty("NameId")
        private String nameId;

        @JsonProperty("SystemId")
        private String systemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyPairIId {
        @JsonProperty("NameId")
        private String nameId;

        @JsonProperty("SystemId")
        private String systemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataDiskIId {
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
