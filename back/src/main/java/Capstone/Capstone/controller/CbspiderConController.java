package Capstone.Capstone.controller;

import Capstone.Capstone.service.CbspiderConService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/spider")
public class CbspiderConController {
    private final CbspiderConService cbspiderConService;

    public CbspiderConController(CbspiderConService cbspiderConService) {
        this.cbspiderConService = cbspiderConService;
    }
    @PostMapping("/aws/{id}")
    public ResponseEntity<String> conAWS(@PathVariable("id")Long id){
        String s = cbspiderConService.conAWS(id);
        return ResponseEntity.ok(s);
    }

    @DeleteMapping("/aws/{id}")
    public ResponseEntity<String> deleteconAWS(@PathVariable("id")Long id){
        String s = cbspiderConService.deleteconAWS(id);
        return ResponseEntity.ok(s);
    }

    @PostMapping("/azure/{id}")
    public ResponseEntity<String> conAzure(@PathVariable("id")Long id){
        String s = cbspiderConService.conAZURE(id);
        return ResponseEntity.ok(s);
    }

    @DeleteMapping("/azure/{id}")
    public ResponseEntity<String> deleteconAzure(@PathVariable("id")Long id){
        String s = cbspiderConService.deleteconAzure(id);
        return ResponseEntity.ok(s);
    }

    @PostMapping("/openstack/{id}")
    public ResponseEntity<String> conOpenstack(@PathVariable("id")Long id){
        String s = cbspiderConService.conOpenStack(id);
        return ResponseEntity.ok(s);
    }

    @DeleteMapping("/openstack/{id}")
    public ResponseEntity<String> deleteconOpenstack(@PathVariable("id")Long id){
        String s = cbspiderConService.deleteconOpenStack(id);
        return ResponseEntity.ok(s);
    }
}
