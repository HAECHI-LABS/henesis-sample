package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.*;
import io.haechi.henesis.assignment.domain.repository.UserWalletRepository;
import io.haechi.henesis.assignment.application.dto.CreateUserWalletDTO;
import io.haechi.henesis.assignment.application.dto.TransferDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange")
public class ExchangeController {

    private final ExchangeApplicationService exchangeApplicationService;
    private final UserWalletRepository userWalletRepository;

    public ExchangeController(ExchangeApplicationService exchangeApplicationService,
                              UserWalletRepository userWalletRepository){
        this.exchangeApplicationService = exchangeApplicationService;
        this.userWalletRepository = userWalletRepository;
    }

    // 사용자 지갑 생성
    @PostMapping("/create-user-wallet")
    @ResponseStatus(value = HttpStatus.OK)
    public CreateWalletDTO createUserWallet(@RequestBody CreateUserWalletDTO createUserWalletDTO){
        return exchangeApplicationService.createUserWallet(createUserWalletDTO);
    }

    // 출금하기
    @PostMapping("/transfer")
    @ResponseStatus(value = HttpStatus.OK)
    public TransferCoinDTO transferCoin(@RequestBody TransferDTO transferDTO){
        return exchangeApplicationService.transferCoin(transferDTO);
    }


    // 집금하기

}
