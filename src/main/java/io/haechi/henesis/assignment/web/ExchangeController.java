package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.application.ExchangeApplicationService;
import io.haechi.henesis.assignment.application.dto.*;
import io.haechi.henesis.assignment.domain.repository.UserWalletRepository;
import io.haechi.henesis.assignment.application.dto.CreateWalletRequestDTO;
import io.haechi.henesis.assignment.application.dto.TransferRequestDTO;
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
    public CreateWalletResponseDTO createUserWallet(@RequestBody CreateWalletRequestDTO createWalletRequestDTO){
        return exchangeApplicationService.createUserWallet(createWalletRequestDTO);
    }

    // 출금하기
    @PostMapping("/transfer")
    @ResponseStatus(value = HttpStatus.OK)
    public TransferResponseDTO transferCoin(@RequestBody TransferRequestDTO transferRequestDTO){
        return exchangeApplicationService.transferCoin(transferRequestDTO);
    }


    // 집금하기
    @PostMapping("/flush")
    @ResponseStatus(value = HttpStatus.OK)
    public FlushResponseDTO flushAll(@RequestBody FlushRequestDTO flushRequestDTO){
        return exchangeApplicationService.flushAll(flushRequestDTO);
    }
}
