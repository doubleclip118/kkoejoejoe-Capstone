package Capstone.Capstone.controller;

import Capstone.Capstone.controller.dto.BlockChainNetworkRequest;
import Capstone.Capstone.controller.dto.BlockChainNetworkResponse;
import Capstone.Capstone.service.BlockChainNetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/network")
public class BlockChainNetworkController {
    private final BlockChainNetworkService blockChainNetworkService;

    public BlockChainNetworkController(BlockChainNetworkService blockChainNetworkService) {
        this.blockChainNetworkService = blockChainNetworkService;
    }

    @GetMapping("/test/{vmid}")
    public ResponseEntity<String> test(@PathVariable("vmid")Long vmId){
        blockChainNetworkService.sftpToEC2Instance(vmId);
        return ResponseEntity.ok("오케이");
    }

    @PostMapping()
    public ResponseEntity<BlockChainNetworkResponse> createNetwork(BlockChainNetworkRequest network){
        BlockChainNetworkResponse blockChainNetworkResponse = blockChainNetworkService.executeStartupScript(
            network);
        return ResponseEntity.ok(blockChainNetworkResponse);
    }


}
