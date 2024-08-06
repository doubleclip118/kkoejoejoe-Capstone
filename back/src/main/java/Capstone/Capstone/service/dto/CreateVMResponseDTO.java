package Capstone.Capstone.service.dto;

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

    private IId IId;

    private String startTime;

    private Region region;

    private String imageType;

    private IId imageIId;

    private String vmSpecName;

    private IId vpcIId;

    private IId subnetIId;

    private List<IId> securityGroupIIds;

    private IId keyPairIId;

    private String rootDiskType;

    private String rootDiskSize;

    private String rootDeviceName;

    private List<IId> dataDiskIIds;

    private String vmBootDisk;
    private String vmBlockDisk;
    private String vmUserId;
    private String vmUserPasswd;
    private String networkInterface;
    private String publicIP;
    private String publicDNS;
    private String privateIP;
    private String privateDNS;
    private String platform;
    private String sshAccessPoint;
    private String accessPoint;
    private List<Tag> tagList;
    private List<KeyValue> keyValueList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IId {
        private String nameId;

        private String systemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Region {
        private String region;

        private String zone;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        private String key;
        private String value;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyValue {
        private String key;

        private String value;
    }
}
