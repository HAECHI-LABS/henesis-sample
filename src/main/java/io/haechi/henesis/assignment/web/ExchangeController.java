package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange")
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



    // 마스터 지갑 잔고 조회
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public MasterWalletBalanceDTO findMasterWalletBalance(@RequestParam String masterWalletId){
        return exchangeApplicationService.findMasterWalletBalanceById(masterWalletId);
    }

    // Flushed 된 트랜잭션 조회
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public FlushedTxDTO findFlushedTx(@RequestParam String txId){
        return exchangeApplicationService.findFlushedTxByTxId(txId);
    }


    // 사용자 지갑 조회 (walletId)
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public UserWalletDTO findUserWalletByWalletId(@RequestParam String walletId){
        return exchangeApplicationService.findUserWalletByWalletId(walletId);
    }

    //출금
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)

}
