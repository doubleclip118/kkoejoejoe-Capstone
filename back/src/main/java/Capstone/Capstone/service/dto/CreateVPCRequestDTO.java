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
public class CreateVPCRequestDTO {

    @NotEmpty
    private String connectionName;

    @NotEmpty
    private ReqInfo reqInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqInfo {
        @NotEmpty
        private String name;

        @NotEmpty
        private String ipv4Cidr;

        private List<SubnetInfo> subnetInfoList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubnetInfo {
        @NotEmpty
        private String name;

        @NotEmpty
        private String ipv4Cidr;
    }
}
