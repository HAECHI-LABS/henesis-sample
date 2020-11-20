package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.FlushedTxDTO;
import io.haechi.henesis.assignment.application.dto.MasterWalletBalanceDTO;
import io.haechi.henesis.assignment.application.dto.UserWalletDTO;
import io.haechi.henesis.assignment.domain.FlushedTx;
import io.haechi.henesis.assignment.domain.MasterWalletBalance;
import io.haechi.henesis.assignment.domain.UserWallet;
import io.haechi.henesis.assignment.domain.ExchangeService;
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


    // 사용자 지갑 생성
    public UserWalletDTO createUserWallet(String walletName){
        UserWallet userWallet = exchangeService.createUserWallet(walletName);
        return modelMapper.map(userWallet, UserWalletDTO.class);
    }

    // 마스터 지갑 잔고 조회
    public MasterWalletBalanceDTO findMasterWalletBalanceById(String masterWalletId){
        MasterWalletBalance masterWalletBalance = exchangeService.findMasterWalletBalanceById(masterWalletId);
        return modelMapper.map(masterWalletBalance, MasterWalletBalanceDTO.class);
    }

    // FLUSH 된 트랜잭션 조회
    public FlushedTxDTO findFlushedTxByTxId(String txId){
        FlushedTx flushedTx = exchangeService.findFlushedTxByTxId(txId);
        return modelMapper.map(flushedTx, FlushedTxDTO.class);
    }

    public UserWalletDTO findUserWalletByWalletId(String walletId){
        UserWallet userWallet = exchangeService.findUserWalletByWalletId(walletId);
        return modelMapper.map(userWallet, UserWalletDTO.class);
    }






}
