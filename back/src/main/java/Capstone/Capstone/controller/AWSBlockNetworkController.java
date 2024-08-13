package Capstone.Capstone.controller;

import Capstone.Capstone.service.AWSVmNetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AWSBlockNetworkController {
    private final AWSVmNetworkService awsVmNetworkService;

    public AWSBlockNetworkController(AWSVmNetworkService awsVmNetworkService) {
        this.awsVmNetworkService = awsVmNetworkService;
    }

    @GetMapping("/api/test/{id}")
    public ResponseEntity<String> test(@PathVariable("id")Long id){
        awsVmNetworkService.sftpToEC2Instance(id);
        return ResponseEntity.ok("오케이");
    }
}
