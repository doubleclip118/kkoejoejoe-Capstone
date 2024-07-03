package Capstone.Capstone.controller;

import Capstone.Capstone.dto.UserDTO;
import Capstone.Capstone.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> UserRegister(@Valid @RequestBody UserDTO userDTO){
        log.info("register");
        UserDTO user = userService.RegisterUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> UserLogin(@Valid @RequestBody UserDTO userDTO){
        log.info("login");
        UserDTO user = userService.UserLogin(userDTO);
        return ResponseEntity.ok(user);
    }

}
