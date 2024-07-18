package Capstone.Capstone.controller;

import Capstone.Capstone.dto.AWSInfoRequest;
import Capstone.Capstone.dto.AWSInfoResponse;
import Capstone.Capstone.dto.AzureInfoRequest;
import Capstone.Capstone.dto.AzureInfoResponse;
import Capstone.Capstone.dto.UserRequest;
import Capstone.Capstone.dto.UserResponse;
import Capstone.Capstone.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<UserResponse> userRegister(@Valid @RequestBody UserRequest userRequest){
        log.info("register");
        UserResponse user = userService.registerUser(userRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> userLogin(@Valid @RequestBody UserRequest userRequest){
        log.info("login");
        UserResponse user = userService.userLogin(userRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/cloud/aws")
    public ResponseEntity<AWSInfoResponse> createAWSInfo(@Valid @RequestBody AWSInfoRequest awsInfoRequest){
        log.info("aws info post");
        AWSInfoResponse awsInfo = userService.createAWSInfo(awsInfoRequest);
        return ResponseEntity.ok(awsInfo);
    }

    @PostMapping("/cloud/azure")
    public ResponseEntity<AzureInfoResponse> createAzureInfo(@Valid @RequestBody AzureInfoRequest azureInfoRequest){
        log.info("azure info post");
        AzureInfoResponse azureInfo = userService.createAzureInfo(azureInfoRequest);
        return ResponseEntity.ok(azureInfo);
    }

    @GetMapping("/cloud/aws/{id}")
    public ResponseEntity<AWSInfoResponse> getAWSInfo(@RequestParam("id") Long id){
        log.info("aws info get");
        AWSInfoResponse awsInfo = userService.getAWSInfo(id);
        return ResponseEntity.ok(awsInfo);
    }

    @GetMapping("/cloud/azure/{id}")
    public ResponseEntity<AzureInfoResponse> getAzureInfo(@RequestParam("id") Long id){
        log.info("azure info get");
        AzureInfoResponse azureInfo = userService.getAzureInfo(id);
        return ResponseEntity.ok(azureInfo);
    }

    @DeleteMapping("/cloud/aws/{id}")
    public ResponseEntity<String> deleteAWSInfo(@RequestParam("id") Long id){
        log.info("aws info delete");
        String s = userService.deleteAWSInfo(id);
        return ResponseEntity.ok(s);
    }

    @DeleteMapping("/cloud/azure/{id}")
    public ResponseEntity<String> deleteAzureInfo(@RequestParam("id") Long id){
        log.info("azure info delete");
        String s = userService.deleteAzureInfo(id);
        return ResponseEntity.ok(s);
    }

    @PutMapping("/cloud/aws/{id}")
    public ResponseEntity<AWSInfoResponse> changeAWSInfo(@RequestParam("id") Long id,
        @RequestBody AWSInfoRequest awsInfoRequest){
        log.info("aws info change");
        AWSInfoResponse awsInfoResponse = userService.changeAWSInfo(id, awsInfoRequest);
        return ResponseEntity.ok(awsInfoResponse);
    }

    @PutMapping("/cloud/azure/{id}")
    public ResponseEntity<AzureInfoResponse> changeAzureInfo(@RequestParam("id") Long id,
        @RequestBody AzureInfoRequest azureInfoRequest){
        log.info("aws info change");
        AzureInfoResponse azureInfoResponse = userService.changeAzureInfo(id, azureInfoRequest);
        return ResponseEntity.ok(azureInfoResponse);
    }
}
