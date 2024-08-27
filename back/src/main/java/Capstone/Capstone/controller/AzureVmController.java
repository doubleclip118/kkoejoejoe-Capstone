package Capstone.Capstone.controller;

import Capstone.Capstone.controller.dto.GetVmDTO;
import Capstone.Capstone.controller.dto.VmInfoRequest;
import Capstone.Capstone.controller.dto.VmInfoResponse;
import Capstone.Capstone.controller.dto.VmcreateResponse;
import Capstone.Capstone.service.AzureVmInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/vm/azure")
public class AzureVmController {
    private final AzureVmInfoService azureVmInfoService;

    public AzureVmController(AzureVmInfoService azureVmInfoService) {
        this.azureVmInfoService = azureVmInfoService;
    }

    @PostMapping()
    public ResponseEntity<VmInfoResponse> postVmInfo(@RequestBody VmInfoRequest vmInfoRequest) {
        VmInfoResponse azureVmInfo = azureVmInfoService.createAzureVmInfo(vmInfoRequest);
        return ResponseEntity.ok(azureVmInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInfo(@PathVariable("id") Long vmid) {
        String vmInfoDTO = azureVmInfoService.deleteAzureVmInfo(vmid);
        return ResponseEntity.ok(vmInfoDTO);
    }

    @PostMapping("/con/{id}")
    public ResponseEntity<VmcreateResponse> createVm(@PathVariable("id") Long id) {
        VmcreateResponse vm = azureVmInfoService.createVm(id);
        log.info("컨트롤러로 들어옴");
        return ResponseEntity.ok(vm);
    }

    @DeleteMapping("/con/{vmid}")
    public ResponseEntity<String> deleteVm(@PathVariable("vmid") Long vmid) {
        azureVmInfoService.deleteVm(vmid);
        return ResponseEntity.ok("성공");
    }

    @GetMapping("/con/{id}")
    public ResponseEntity<List<GetVmDTO>> getVm(@PathVariable("id") Long id) {
        List<GetVmDTO> vmDTOList = azureVmInfoService.getVmDTOList(id);
        return ResponseEntity.ok(vmDTOList);
    }
}
