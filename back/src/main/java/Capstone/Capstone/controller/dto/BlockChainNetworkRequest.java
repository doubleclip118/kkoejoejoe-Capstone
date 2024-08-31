package Capstone.Capstone.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockChainNetworkRequest {
    @NotNull(message = "Network name cannot be null")
    private String networkName;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "CA CSP cannot be null")
    private String caCSP;

    @NotNull(message = "CA IP cannot be null")
    private String caIP;

    @NotNull(message = "CA Secret Key cannot be null")
    private String caSecretKey;

    @NotNull(message = "ORG IP cannot be null")
    private String orgIP;

    @NotNull(message = "ORG CSP cannot be null")
    private String orgCSP;

    @NotNull(message = "ORG Secret Key cannot be null")
    private String orgSecretKey;
}
