package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.*;
import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.application.dto.CreateWalletRequestDTO;
import io.haechi.henesis.assignment.domain.arguments.CreateUserArguments;
import io.haechi.henesis.assignment.application.dto.TransferRequestDTO;
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


    /**
     * 사용자 지갑 생성하기
     * @param createWalletRequestDTO
     * @return
     */
    public CreateWalletResponseDTO createUserWallet(CreateWalletRequestDTO createWalletRequestDTO){
        CreateUserArguments createUserArguments = CreateUserArguments.builder()
                .name(createWalletRequestDTO.getWalletName())
                .passphrase(createWalletRequestDTO.getPassphrase())
                .build();
        UserWallet userWallet = exchangeService.createUserWallet(createUserArguments);
        return modelMapper.map(userWallet, CreateWalletResponseDTO.class);
    }

    /**
     * 출금 하기
     * @param transferRequestDTO
     * @return
     */
    public TransferResponseDTO transferCoin(TransferRequestDTO transferRequestDTO) {

        Transaction transaction = exchangeService.transfer(
                transferRequestDTO.getUserWalletId(),
                transferRequestDTO.getAmount(),
                transferRequestDTO.getTo(),
                transferRequestDTO.getTicker(),
                transferRequestDTO.getPassphrase()
                );
        return modelMapper.map(transaction, TransferResponseDTO.class);
    }

    /**
     * 집금 하기
     * @param flushRequestDTO
     * @return
     */
    public FlushResponseDTO flushAll(FlushRequestDTO flushRequestDTO) {

        Transaction transaction = exchangeService.flush(
                flushRequestDTO.getTicker(),
                flushRequestDTO.getPassphrase()
        );

        return modelMapper.map(transaction, FlushResponseDTO.class);
    }
}
