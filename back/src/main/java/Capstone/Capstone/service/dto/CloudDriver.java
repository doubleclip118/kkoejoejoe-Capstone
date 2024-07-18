package Capstone.Capstone.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CloudDriver {
    private String DriverName;
    private String ProviderName;
    private String DriverLibFileName;
}
