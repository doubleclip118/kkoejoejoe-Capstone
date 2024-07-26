package Capstone.Capstone.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueInfo {
    @JsonProperty("Key")
    private String key;

    @JsonProperty("Value")
    private String value;
}
