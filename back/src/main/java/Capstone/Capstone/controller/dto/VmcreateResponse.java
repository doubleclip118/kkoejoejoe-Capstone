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
public class VmcreateResponse {
    @NotEmpty
    private Long userId;
    @NotEmpty
    private Long vmId;
    @NotEmpty
    private String privateKey;
    @NotEmpty
    private String ip;
}
