package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.BalanceDto;
import io.haechi.henesis.assignment.application.dto.CreateDepositAddressRequest;
import io.haechi.henesis.assignment.application.dto.DepositAddressDto;
import io.haechi.henesis.assignment.application.dto.FlushDto;
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
    @ResponseStatus(value = HttpStatus.OK)
    public List<DepositAddressDto> getDepositAddresses(@RequestParam final String blockchain) {
        return this.exchangeApplicationService.getDepositAddresses(Blockchain.of(blockchain));
    }

    @GetMapping("/deposit-addresses/{depositAddressId}")
    @ResponseStatus(value = HttpStatus.OK)
    public DepositAddressDto getDepositAddress(
            @PathVariable final Long depositAddressId,
            @RequestParam final String blockchain
    ) {
        return this.exchangeApplicationService.getDepositAddress(depositAddressId, Blockchain.of(blockchain));
    }

    @GetMapping("/deposit-addresses/{depositAddressId}/balances")
    @ResponseStatus(value = HttpStatus.OK)
    public List<BalanceDto> getDepositAddressBalacnes(
            @PathVariable final Long depositAddressId,
            @RequestParam final String blockchain
    ) {
        return this.exchangeApplicationService.getDepositAddressBalances(depositAddressId, Blockchain.of(blockchain));
    }

    @PostMapping("/deposit-addresses")
    @ResponseStatus(value = HttpStatus.CREATED)
    public DepositAddressDto createDepositAddress(
            @RequestParam final String blockchain,
            @RequestBody final CreateDepositAddressRequest createWalletRequest
    ) {
        return this.exchangeApplicationService.createDepositAddress(
                Blockchain.of(blockchain),
                createWalletRequest
        );
    }

    @GetMapping("/transfers/{transferId}")
    @ResponseStatus(value = HttpStatus.OK)
    public TransferDto getTransfer(@PathVariable final Long transferId) {
        return this.exchangeApplicationService.getTransfer(transferId);
    }

    @PostMapping("/deposit-addresses/{depositAddressId}/transfer")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TransferDto transfer(
            @PathVariable final Long depositAddressId,
            @RequestParam final String blockchain,
            @RequestBody final TransferRequest transferRequest
    ) {
        return this.exchangeApplicationService.transfer(
                depositAddressId,
                Blockchain.of(blockchain),
                transferRequest
        );
    }

    @PostMapping("/flush")
    @ResponseStatus(value = HttpStatus.CREATED)
    public FlushDto flush(
            @RequestParam final String blockchain,
            @RequestBody final FlushRequest request
    ) {
        return this.exchangeApplicationService.flush(
                Blockchain.of(blockchain),
                request.getDepositAddressIds(),
                request.getSymbol()
        );
    }
}
