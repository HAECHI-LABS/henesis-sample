package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.CreateDepositAddressRequest;
import io.haechi.henesis.assignment.application.dto.DepositAddressDto;
import io.haechi.henesis.assignment.application.dto.FlushRequest;
import io.haechi.henesis.assignment.application.dto.TransferDto;
import io.haechi.henesis.assignment.application.dto.TransferRequest;
import io.haechi.henesis.assignment.domain.Blockchain;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {
    private final ExchangeApplicationService exchangeApplicationService;

    public ExchangeController(
            ExchangeApplicationService exchangeApplicationService
    ) {
        this.exchangeApplicationService = exchangeApplicationService;
    }

    @GetMapping("/deposit-addresses")
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<DepositAddressDto> getDepositAddresses(@RequestParam String blockchain) {
        return this.exchangeApplicationService.getDepositAddresses(Blockchain.of(blockchain));
    }

    @PostMapping("/deposit-addresses")
    @ResponseStatus(value = HttpStatus.CREATED)
    public DepositAddressDto createDepositAddress(
            @RequestParam String blockchain,
            @RequestBody CreateDepositAddressRequest createWalletRequest
    ) {
        return this.exchangeApplicationService.createDepositAddress(
                Blockchain.of(blockchain),
                createWalletRequest
        );
    }

    @PostMapping("/deposit-addresses/{depositAddressId}/transfer")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TransferDto transfer(
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
    public TransferDto flush(
            @RequestParam String blockchain,
            @RequestBody FlushRequest request
    ) {
        return this.exchangeApplicationService.flush(
                Blockchain.of(blockchain),
                request.getDepositAddressIds(),
                request.getSymbol()
        );
    }
}
