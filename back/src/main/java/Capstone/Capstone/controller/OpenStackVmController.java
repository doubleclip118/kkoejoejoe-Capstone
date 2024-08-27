package Capstone.Capstone.controller;

import Capstone.Capstone.controller.dto.GetVmDTO;
import Capstone.Capstone.controller.dto.OpenstackVmRequest;
import Capstone.Capstone.controller.dto.OpenstackVmResponse;
import Capstone.Capstone.controller.dto.VmInfoRequest;
import Capstone.Capstone.controller.dto.VmInfoResponse;
import Capstone.Capstone.controller.dto.VmcreateResponse;
import Capstone.Capstone.service.OpenstackVmInfoService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/vm/openstack")
public class OpenStackVmController {
    private final OpenstackVmInfoService openstackVmInfoService;

    public OpenStackVmController(OpenstackVmInfoService openstackVmInfoService) {
        this.openstackVmInfoService = openstackVmInfoService;
    }

    @PostMapping()
    public ResponseEntity<OpenstackVmResponse> postVmInfo(@RequestBody OpenstackVmRequest vmInfoRequest ) {
        OpenstackVmResponse openStackVmInfo = openstackVmInfoService.createOpenStackVmInfo(
            vmInfoRequest);
        return ResponseEntity.ok(openStackVmInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInfo(@PathVariable("id") Long vmid) {
        String vmInfoDTO = openstackVmInfoService.deleteOpenStackVmInfo(vmid);
        return ResponseEntity.ok(vmInfoDTO);
    }

    @PostMapping("/con/{id}")
    public ResponseEntity<VmcreateResponse> createVm(@PathVariable("id") Long id) {
        VmcreateResponse vm = openstackVmInfoService.createVm(id);
        log.info("컨트롤러로 들어옴");
        return ResponseEntity.ok(vm);
    }

    @DeleteMapping("/con/{vmid}")
    public ResponseEntity<String> deleteVm(@PathVariable("vmid") Long vmid) {
        openstackVmInfoService.deleteVm(vmid);
        return ResponseEntity.ok("성공");
    }

    @GetMapping("/con/{id}")
    public ResponseEntity<List<GetVmDTO>> getVm(@PathVariable("id") Long id) {
        List<GetVmDTO> vmDTOList = openstackVmInfoService.getVmDTOList(id);
        return ResponseEntity.ok(vmDTOList);
    }
}
