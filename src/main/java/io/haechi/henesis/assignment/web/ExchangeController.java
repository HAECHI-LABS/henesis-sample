package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.CreateDepositAddressRequest;
import io.haechi.henesis.assignment.application.dto.CreateDepositAddressResponse;
import io.haechi.henesis.assignment.application.dto.FlushRequest;
import io.haechi.henesis.assignment.application.dto.FlushResponse;
import io.haechi.henesis.assignment.application.dto.TransferRequest;
import io.haechi.henesis.assignment.application.dto.TransferResponse;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "Henesis")
@RestController
@RequestMapping("exchange")
public class ExchangeController {
    private final ExchangeApplicationService exchangeApplicationService;

    public ExchangeController(
            ExchangeApplicationService exchangeApplicationService
    ) {
        this.exchangeApplicationService = exchangeApplicationService;
    }

    @PostMapping("/deposit-address")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateDepositAddressResponse createEthWallet(
            @RequestParam String blockchain,
            @RequestBody CreateDepositAddressRequest createWalletRequest
    ) {
        return this.exchangeApplicationService.createDepositAddress(
                Blockchain.of(blockchain),
                createWalletRequest
        );
    }

    @PostMapping("/deposit-address/{depositAddressId}/transfer")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TransferResponse transferEth(
            @PathVariable Long depositAddressId,
            @RequestParam String blockchain,
            @RequestBody TransferRequest transferRequest
    ) {
        return this.exchangeApplicationService.transfer(
                depositAddressId,
                Blockchain.of(blockchain),
                transferRequest
        );
    }

    @PostMapping("/flush")
    @ResponseStatus(value = HttpStatus.CREATED)
    public FlushResponse flushKlay(
            @RequestParam String blockchain,
            @RequestBody FlushRequest flushRequest
    ) {
        return this.exchangeApplicationService.flush(Blockchain.of(blockchain));
    }
}
