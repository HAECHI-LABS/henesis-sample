package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.FlushedTxDTO;
import io.haechi.henesis.assignment.application.dto.MasterWalletDTO;
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

    // 사용자 지갑 생성
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public UserWalletDTO createUserWallet(@RequestBody WalletCreateRequestDTO walletCreateRequestDTO){
        return exchangeApplicationService.createUserWallet(walletCreateRequestDTO.getMasterWalletId());
    }

    // 마스터 지갑 잔고 조회
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public MasterWalletDTO findMasterWalletBalance(@RequestParam String masterWalletId){
        return exchangeApplicationService.findMasterWalletBalanceById(masterWalletId);
    }

    // Flushed 된 트랜잭션 조회
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public FlushedTxDTO findFlushedTx(@RequestParam String txId){
        return exchangeApplicationService.findFlushedTxByTxId(txId);
    }


    // 코인/토큰 전송하기
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public UserWalletDTO findUserWalletByWalletId(@RequestParam String walletId){
        return exchangeApplicationService.findUserWalletByWalletId(walletId);
    }

    //출금

    //집금

}
