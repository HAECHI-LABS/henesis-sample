package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.*;
import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.application.dto.CreateWalletDTO;
import io.haechi.henesis.assignment.domain.arguments.CreateUserArguments;
import io.haechi.henesis.assignment.domain.arguments.TransferArguments;
import io.haechi.henesis.assignment.application.dto.TransferDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ExchangeApplicationService {
    private final ExchangeService exchangeService;
    private final ModelMapper modelMapper;

    public ExchangeApplicationService(ExchangeService exchangeService, ModelMapper modelMapper) {
        this.exchangeService = exchangeService;
        this.modelMapper = modelMapper;
    }


    // 지갑 생성하기
    public CreateWalletResponseDTO createUserWallet(CreateWalletDTO createWalletDTO){
        CreateUserArguments createUserArguments = CreateUserArguments.builder()
                .name(createWalletDTO.getWalletName())
                .passphrase(createWalletDTO.getPassphrase())
                .build();
        UserWallet userWallet = exchangeService.createUserWallet(createUserArguments);
        return modelMapper.map(userWallet, CreateWalletResponseDTO.class);
    }

    // 출금 하기
    public TransferResponseDTO transferCoin(TransferDTO transferDTO) {

        TransferArguments transferArguments = TransferArguments.builder()
                .ticker(transferDTO.getTicker())
                .to(transferDTO.getTo())
                .amount(transferDTO.getAmount())
                .passphrase(transferDTO.getPassphrase())
                .build();

        Transaction transaction = exchangeService.transfer(transferDTO.getUserWalletId(),
                transferDTO.getAmount(),
                transferDTO.getTo(),
                transferDTO.getTicker(),
                transferDTO.getPassphrase()
                );
        return modelMapper.map(transaction, TransferResponseDTO.class);
    }

}
