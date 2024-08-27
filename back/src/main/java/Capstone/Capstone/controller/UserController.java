package Capstone.Capstone.controller;

import Capstone.Capstone.controller.dto.AWSInfoRequest;
import Capstone.Capstone.controller.dto.AWSInfoResponse;
import Capstone.Capstone.controller.dto.AzureInfoRequest;
import Capstone.Capstone.controller.dto.AzureInfoResponse;
import Capstone.Capstone.controller.dto.OpenStackInfoRequest;
import Capstone.Capstone.controller.dto.UserRequest;
import Capstone.Capstone.controller.dto.UserResponse;
import Capstone.Capstone.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/cloud/aws/{id}")
    public ResponseEntity<AWSInfoResponse> getAWSInfo(@PathVariable("id") Long id){
        log.info("aws info get");
        AWSInfoResponse awsInfo = userService.getAWSInfo(id);
        return ResponseEntity.ok(awsInfo);
    }

    @DeleteMapping("/cloud/aws/{id}")
    public ResponseEntity<String> deleteAWSInfo(@PathVariable("id") Long id){
        log.info("aws info delete");
        String s = userService.deleteAWSInfo(id);
        return ResponseEntity.ok(s);
    }

    @PutMapping("/cloud/aws/{id}")
    public ResponseEntity<AWSInfoResponse> changeAWSInfo(@PathVariable("id") Long id,
        @RequestBody AWSInfoRequest awsInfoRequest){
        log.info("aws info change");
        AWSInfoResponse awsInfoResponse = userService.changeAWSInfo(id, awsInfoRequest);
        return ResponseEntity.ok(awsInfoResponse);
    }

    @GetMapping("/cloud/azure/{id}")
    public ResponseEntity<AzureInfoResponse> getAzureInfo(@PathVariable("id") Long id){
        log.info("azure info get");
        AzureInfoResponse azureInfo = userService.getAzureInfo(id);
        return ResponseEntity.ok(azureInfo);
    }

    @PostMapping("/cloud/azure")
    public ResponseEntity<AzureInfoResponse> createAzureInfo(@Valid @RequestBody AzureInfoRequest azureInfoRequest){
        log.info("azure info post");
        AzureInfoResponse azureInfo = userService.createAzureInfo(azureInfoRequest);
        return ResponseEntity.ok(azureInfo);
    }

    @DeleteMapping("/cloud/azure/{id}")
    public ResponseEntity<String> deleteAzureInfo(@PathVariable("id") Long id){
        log.info("azure info delete");
        String s = userService.deleteAzureInfo(id);
        return ResponseEntity.ok(s);
    }

    @PutMapping("/cloud/azure/{id}")
    public ResponseEntity<AzureInfoResponse> changeAzureInfo(@PathVariable("id") Long id,
        @RequestBody AzureInfoRequest azureInfoRequest){
        log.info("aws info change");
        AzureInfoResponse azureInfoResponse = userService.changeAzureInfo(id, azureInfoRequest);
        return ResponseEntity.ok(azureInfoResponse);
    }

    @PostMapping("/cloud/openstack")
    public ResponseEntity<OpenStackInfoRequest> createOpenstackInfo(@RequestBody OpenStackInfoRequest openStackInfoRequest){
        OpenStackInfoRequest openstackInfo = userService.createOpenstackInfo(openStackInfoRequest);
        return ResponseEntity.ok(openstackInfo);
    }

    @GetMapping("/cloud/openstack/{id}")
    public ResponseEntity<OpenStackInfoRequest> getOpenstackInfo(@PathVariable("id") Long id){
        log.info("openstack info get");
        OpenStackInfoRequest openStackInfo = userService.getOpenStackInfo(id);
        return ResponseEntity.ok(openStackInfo);
    }

    @DeleteMapping("/cloud/openstack/{id}")
    public ResponseEntity<String> deleteOpenstackInfo(@PathVariable("id") Long id){
        log.info("openstack info delete");
        String s = userService.deleteOpenStackInfo(id);
        return ResponseEntity.ok(s);
    }

    @PutMapping("/cloud/openstack/{id}")
    public ResponseEntity<OpenStackInfoRequest> changeOpenStackInfo(@PathVariable("id") Long id,
        @RequestBody OpenStackInfoRequest openStackInfoRequest){
        log.info("openstack info change");
        OpenStackInfoRequest openStack = userService.changeOpenStackInfo(id, openStackInfoRequest);
        return ResponseEntity.ok(openStack);
    }
}
