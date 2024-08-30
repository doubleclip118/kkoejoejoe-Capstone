package Capstone.Capstone.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetVmDTO {
    private Long vmId;
    private String vmName;
    private String ip;
    private String privatekey;
}
