package Capstone.Capstone.service.dto;

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
    private String connectionName;

    @NotNull
    private ReqInfo reqInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReqInfo {
        @NotNull
        @NotEmpty
        private String name;
    }
}
