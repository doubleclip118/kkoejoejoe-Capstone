package Capstone.Capstone.controller;

import Capstone.Capstone.dto.AWSInfoRequest;
import Capstone.Capstone.dto.AWSInfoResponse;
import Capstone.Capstone.dto.AzureInfoRequest;
import Capstone.Capstone.dto.AzureInfoResponse;
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
    public ResponseEntity<UserDTO> userRegister(@Valid @RequestBody UserDTO userDTO){
        log.info("register");
        UserDTO user = userService.registerUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> userLogin(@Valid @RequestBody UserDTO userDTO){
        log.info("login");
        UserDTO user = userService.userLogin(userDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/cloud/aws")
    public ResponseEntity<AWSInfoResponse> createAWSInfo(@Valid @RequestBody AWSInfoRequest awsInfoRequest){
        log.info("aws info post");
        AWSInfoResponse awsInfo = userService.createAWSInfo(awsInfoRequest);
        return ResponseEntity.ok(awsInfo);
    }

    @PostMapping("/cloud/azure")
    public ResponseEntity<AzureInfoResponse> createAWSInfo(@Valid @RequestBody AzureInfoRequest azureInfoRequest){
        log.info("azure info post");
        AzureInfoResponse azureInfo = userService.createAzureInfo(azureInfoRequest);
        return ResponseEntity.ok(azureInfo);
    }
}
