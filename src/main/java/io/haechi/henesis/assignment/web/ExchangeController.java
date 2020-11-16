package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.UserWalletDTO;
import io.haechi.henesis.assignment.application.dto.WalletCreateRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange")
public class ExchangeController {

    private final ExchangeApplicationService exchangeApplicationService;

    public ExchangeController(ExchangeApplicationService exchangeApplicationService){
        this.exchangeApplicationService = exchangeApplicationService;
    }

    //지갑생성
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public UserWalletDTO createUserWallet(@RequestBody WalletCreateRequestDTO walletCreateRequestDTO){
        return exchangeApplicationService.createUserWallet(walletCreateRequestDTO.getMasterWalletId());
    }

    //입금

    //출금

    //집금

}
