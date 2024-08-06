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
public class CreateVMRequestDTO {

    private String connectionName;

    private ReqInfo reqInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqInfo {
        private String name;

        private String imageName;

        private String vpcName;

        private String subnetName;

        private List<String> securityGroupNames;

        private String vmSpecName;

        private String keyPairName;
    }
}
