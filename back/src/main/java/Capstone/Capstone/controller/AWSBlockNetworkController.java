package Capstone.Capstone.controller;

import Capstone.Capstone.service.AWSVmNetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AWSBlockNetworkController {
    private final AWSVmNetworkService awsVmNetworkService;

    public AWSBlockNetworkController(AWSVmNetworkService awsVmNetworkService) {
        this.awsVmNetworkService = awsVmNetworkService;
    }

    @GetMapping("/api/test")
    public ResponseEntity<String> test(){
        awsVmNetworkService.connectToEC2Instance(5L);
        return ResponseEntity.ok("오케이");
    }
}
