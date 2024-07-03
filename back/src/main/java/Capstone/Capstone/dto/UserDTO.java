package Capstone.Capstone.dto;

import Capstone.Capstone.domain.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public User UserconvertToEntity(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.username);
        user.setPassword(userDTO.password);
        return user;
    }

}
