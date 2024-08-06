package Capstone.Capstone.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateKeyPairRequestDTO {

    @NotEmpty
    @JsonProperty("ConnectionName")
    private String connectionName;

    @NotNull
    @JsonProperty("ReqInfo")
    private ReqInfo reqInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqInfo {
        @NotNull
        @NotEmpty
        @JsonProperty("Name")
        private String name;
    }

}
