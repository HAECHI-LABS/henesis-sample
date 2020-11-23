package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.*;
import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.application.dto.CreateUserWalletDTO;
import io.haechi.henesis.assignment.infra.dto.TransactionDTO;
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
    public CreateWalletDTO createUserWallet(CreateUserWalletDTO createUserWalletDTO){
        UserWallet userWallet = exchangeService.createUserWallet(createUserWalletDTO);
        return modelMapper.map(userWallet, CreateWalletDTO.class);
    }

    // 출금 하기
    public TransferCoinDTO transferCoin(TransferDTO transferDTO) {
        Transaction transaction = exchangeService.transferCoin(transferDTO);
        return modelMapper.map(transaction, TransferCoinDTO.class);
    }


    //사용자 지갑 잔고 조회
    public CreateWalletDTO findUserWalletByWalletId(String walletId){
        UserWallet userWallet = exchangeService.findUserWalletByWalletId(walletId);
        return modelMapper.map(userWallet, CreateWalletDTO.class);
    }


    // FLUSH 된 트랜잭션 조회
    public TransactionDTO findFlushedTxByTxId(String txId){
        FlushedTx flushedTx = exchangeService.findFlushedTxByTxId(txId);
        return modelMapper.map(flushedTx, TransactionDTO.class);
    }


}
