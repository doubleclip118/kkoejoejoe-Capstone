package Capstone.Capstone.controller;

import Capstone.Capstone.service.BlockChainNetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockChainNetworkController {
    private final BlockChainNetworkService blockChainNetworkService;

    public BlockChainNetworkController(BlockChainNetworkService blockChainNetworkService) {
        this.blockChainNetworkService = blockChainNetworkService;
    }

    @GetMapping("/api/test/{vmid}")
    public ResponseEntity<String> test(@PathVariable("vmid")Long vmId){
        blockChainNetworkService.sftpToEC2Instance(vmId);
        return ResponseEntity.ok("오케이");
    }
}
