package Capstone.Capstone.controller;

import Capstone.Capstone.service.CbspiderConService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> conAWS(@Param("id")Long id){
        String s = cbspiderConService.conAWS(id);
        return ResponseEntity.ok(s);
    }
}
