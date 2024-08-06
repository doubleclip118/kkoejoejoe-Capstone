package Capstone.Capstone.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityGroupRuleDTO {

    private String fromPort;

    private String toPort;

    private String ipProtocol;

    private String direction;
}
