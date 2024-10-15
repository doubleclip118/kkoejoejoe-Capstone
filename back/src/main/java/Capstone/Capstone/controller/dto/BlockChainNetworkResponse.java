package Capstone.Capstone.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockChainNetworkResponse {
    @NotEmpty
    private Long networkId;

    @NotEmpty
    private String networkName;

    @NotEmpty
    private String caCSP;

    @NotEmpty
    private String caIp;

    @NotEmpty
    private String orgCSP;

    @NotEmpty
    private String orgIp;

}
