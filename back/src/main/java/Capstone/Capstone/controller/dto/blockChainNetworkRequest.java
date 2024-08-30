package Capstone.Capstone.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class blockChainNetworkRequest {

    @NotEmpty
    private String networkName;

    @NotEmpty
    private Long userId;

    @NotEmpty
    private String caCSP;
    @NotEmpty
    private String caIP;
    @NotEmpty
    private String caSecretKey;

    @NotEmpty
    private String orgIP;
    @NotEmpty
    private String orgCSP;
}
