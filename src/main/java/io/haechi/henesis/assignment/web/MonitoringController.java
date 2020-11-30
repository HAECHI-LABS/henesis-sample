package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.MonitoringApplicationService;
import io.haechi.henesis.assignment.application.dto.CreateWalletRequestDTO;
import io.haechi.henesis.assignment.application.dto.CreateWalletResponseDTO;
import io.haechi.henesis.assignment.application.dto.MonitoringRequestDTO;
import io.haechi.henesis.assignment.application.dto.MonitoringResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("monitoring")
public class MonitoringController {

    private MonitoringApplicationService monitoringApplicationService;

    public MonitoringController(MonitoringApplicationService monitoringApplicationService){
        this.monitoringApplicationService = monitoringApplicationService;
    }

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public void getValueTransferEvents() {
        monitoringApplicationService.getValueTransferEvents();
    }
}
