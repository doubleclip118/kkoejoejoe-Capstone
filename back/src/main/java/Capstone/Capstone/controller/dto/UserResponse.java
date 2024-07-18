package Capstone.Capstone.controller.dto;

import Capstone.Capstone.domain.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    @NotNull
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public User UserconvertToEntity(UserResponse userDTO){
        User user = new User();
        user.setUsername(userDTO.username);
        user.setPassword(userDTO.password);
        return user;
    }

}
