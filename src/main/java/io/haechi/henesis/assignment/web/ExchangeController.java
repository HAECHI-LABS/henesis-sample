package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange")
public class ExchangeController {

    private final ExchangeApplicationService exchangeApplicationService;

    public ExchangeController(ExchangeApplicationService exchangeApplicationService) {
        this.exchangeApplicationService = exchangeApplicationService;
    }

    // 사용자 지갑 생성
    @PostMapping("/create-user-wallet")
    @ResponseStatus(value = HttpStatus.OK)
    public CreateWalletResponseDTO createUserWallet(@RequestBody CreateWalletRequestDTO createWalletRequestDTO) {
        return exchangeApplicationService.createUserWallet(createWalletRequestDTO);
    }

    // 출금하기
    @PostMapping("/transfer")
    @ResponseStatus(value = HttpStatus.OK)
    public TransferResponseDTO transferCoin(@RequestBody TransferRequestDTO transferRequestDTO) {
        return exchangeApplicationService.transfer(transferRequestDTO);
    }


    // 집금하기
    @PostMapping("/flush")
    @ResponseStatus(value = HttpStatus.OK)
    public FlushResponseDTO flush(@RequestBody FlushRequestDTO flushRequestDTO) {
        return exchangeApplicationService.flush(flushRequestDTO);
    }



    @PostMapping("/updateTransactionList")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateTransactionList() {
        exchangeApplicationService.updateTransactionList();
    }


    @PostMapping("/updateUserWalletList")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateWalletList() {
        exchangeApplicationService.updateWalletList();
    }
}
