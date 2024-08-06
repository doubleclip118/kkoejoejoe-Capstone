package Capstone.Capstone.controller;


import Capstone.Capstone.service.AWSVmInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/vm/aws")
public class AWSVmController {
    private final AWSVmInfoService awsVmInfoService;

    public AWSVmController(AWSVmInfoService awsVmInfoService) {
        this.awsVmInfoService = awsVmInfoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVmInfo(){
        return ResponseEntity.ok(null);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInfo(){
        return ResponseEntity.ok(null);
    }
}
