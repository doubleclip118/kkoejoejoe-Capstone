package Capstone.Capstone.controller.dto;

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
public class UserRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public User UserconvertToEntity(UserRequest userRequest){
        User user = new User();
        user.setUsername(userRequest.username);
        user.setPassword(userRequest.password);
        return user;
    }

}
