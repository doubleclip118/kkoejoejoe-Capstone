package Capstone.Capstone.controller;


import Capstone.Capstone.controller.dto.VmInfoDTO;
import Capstone.Capstone.controller.dto.VmInfoResponse;
import Capstone.Capstone.service.AWSVmInfoService;
import Capstone.Capstone.service.AzureVmInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/vm/azure")
public class AzureVmController {
    private final AzureVmInfoService azureVmInfoService;

    public AzureVmController(AzureVmInfoService azureVmInfoService) {
        this.azureVmInfoService = azureVmInfoService;
    }

    @PostMapping()
    public ResponseEntity<VmInfoResponse> getVmInfo(@RequestBody VmInfoDTO vmInfoDTO){
        VmInfoResponse azureVmInfo = azureVmInfoService.createAzureVmInfo(vmInfoDTO);
        return ResponseEntity.ok(azureVmInfo);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInfo(@PathVariable("id")Long vmid){
        String vmInfoDTO = azureVmInfoService.deleteAzureVmInfo(vmid);
        return ResponseEntity.ok(vmInfoDTO);
    }
}
