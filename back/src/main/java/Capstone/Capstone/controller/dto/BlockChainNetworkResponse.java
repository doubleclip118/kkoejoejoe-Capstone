package Capstone.Capstone.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockChainNetworkResponse {

    @NotEmpty
    private String networkName;

    @NotEmpty
    private Long userId;
}
