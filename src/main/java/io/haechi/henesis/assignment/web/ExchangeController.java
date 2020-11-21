package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeApplicationService exchangeApplicationService;

    public ExchangeController(ExchangeApplicationService exchangeApplicationService){
        this.exchangeApplicationService = exchangeApplicationService;
    }

    // 사용자 지갑 생성
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public UserWalletDTO createUserWallet(@RequestBody String walletName){
        return exchangeApplicationService.createUserWallet(walletName);
    }

    // UserWallet 정보 저장

    // 마스터 지갑 잔고 조회

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public MasterWalletBalanceDTO findMasterWalletBalance(@RequestParam String masterWalletId){
        return exchangeApplicationService.findMasterWalletBalanceById(masterWalletId);
    }

    // UserWallet  walletBalance 조회



}
