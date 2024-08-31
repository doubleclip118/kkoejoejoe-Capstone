package Capstone.Capstone.controller;

import Capstone.Capstone.controller.dto.BlockChainNetworkRequest;
import Capstone.Capstone.controller.dto.BlockChainNetworkResponse;
import Capstone.Capstone.service.BlockChainNetworkService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<BlockChainNetworkResponse> createNetwork(@RequestBody BlockChainNetworkRequest network){
        BlockChainNetworkResponse blockChainNetworkResponse = blockChainNetworkService.postNetwork(
            network);
        return ResponseEntity.ok(blockChainNetworkResponse);
    }
    @DeleteMapping("/{networkId}")
    public ResponseEntity<String> deleteNetwork(@PathVariable("networkId")Long networkId){
        String s = blockChainNetworkService.deleteNetwork(networkId);
        return ResponseEntity.ok(s);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<BlockChainNetworkResponse>> getNetworkList(@PathVariable("userId")Long userId){
        List<BlockChainNetworkResponse> network = blockChainNetworkService.getNetwork(userId);
        return ResponseEntity.ok(network);
    }

}
