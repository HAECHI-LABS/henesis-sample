package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.btc.BtcMonitoringApplicationService;
import io.haechi.henesis.assignment.application.ethklay.EthMonitoringApplicationService;
import io.haechi.henesis.assignment.application.ethklay.KlayMonitoringApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monitoring")
public class MonitoringController {

    private final BtcMonitoringApplicationService btcMonitoringApplicationService;
    private final EthMonitoringApplicationService ethMonitoringApplicationService;
    private final KlayMonitoringApplicationService klayExchangeApplicationService;


    public MonitoringController(
            BtcMonitoringApplicationService btcMonitoringApplicationService,
            EthMonitoringApplicationService ethMonitoringApplicationService,
            KlayMonitoringApplicationService klayExchangeApplicationService
    ) {
        this.btcMonitoringApplicationService = btcMonitoringApplicationService;
        this.ethMonitoringApplicationService = ethMonitoringApplicationService;
        this.klayExchangeApplicationService = klayExchangeApplicationService;
    }

    @GetMapping("btc/update")
    public void MonitoringBtc() {
        btcMonitoringApplicationService.updateTransactions();
    }

    @GetMapping("eth/update")
    @ResponseStatus(value = HttpStatus.OK)
    public void monitoringEthTransactions() {
        ethMonitoringApplicationService.updateTransactions();
        ethMonitoringApplicationService.updateFlushedTransactionStatus();
        ethMonitoringApplicationService.updateWalletStatus();
    }

    @GetMapping("klay/update")
    @ResponseStatus(value = HttpStatus.OK)
    public void monitoringKlayTransactions() {
        klayExchangeApplicationService.updateTransactions();
        klayExchangeApplicationService.updateFlushedTransactionStatus();
        klayExchangeApplicationService.updateWalletStatus();
    }
}
