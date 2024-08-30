package Capstone.Capstone.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class blockChainNetworkResponse {

    @NotEmpty
    private String networkName;

    @NotEmpty
    private Long userId;
}
