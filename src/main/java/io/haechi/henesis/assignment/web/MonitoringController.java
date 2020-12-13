package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.ethKlayApplication.MonitoringApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monitoring")
public class MonitoringController {

    private final MonitoringApplicationService monitoringApplicationService;

    public MonitoringController(MonitoringApplicationService monitoringApplicationService){
        this.monitoringApplicationService = monitoringApplicationService;
    }

    @PostMapping("/transactions")
    @ResponseStatus(value = HttpStatus.OK)
    public void getValueTransferEvents() {
        monitoringApplicationService.getEthValueTransferEvents();
    }

    @PostMapping("/user-wallets")
    @ResponseStatus(value = HttpStatus.OK)
    public void getAllUserWalletInfo(){monitoringApplicationService.getUserWalletInfo();}

}
