package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.FlushedTxDTO;
import io.haechi.henesis.assignment.application.dto.MasterWalletDTO;
import io.haechi.henesis.assignment.application.dto.UserWalletDTO;
import io.haechi.henesis.assignment.domain.FlushedTx;
import io.haechi.henesis.assignment.domain.MasterWallet;
import io.haechi.henesis.assignment.domain.UserWallet;
import io.haechi.henesis.assignment.domain.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ExchangeApplicationService {
    private final WalletService walletService;
    private final ModelMapper modelMapper;

    public ExchangeApplicationService(WalletService userWalletService, ModelMapper modelMapper) {
        this.walletService = userWalletService;
        this.modelMapper = modelMapper;
    }


    // 사용자 지갑 생성
    public UserWalletDTO createUserWallet(String masterWalletId){
        UserWallet userWallet = walletService.createUserWallet(masterWalletId);
        return modelMapper.map(userWallet, UserWalletDTO.class);
    }

    // 마스터 지갑 조회
    public MasterWalletDTO findMasterWalletBalanceById(String masterWalletId){
        MasterWallet masterWallet = walletService.findMasterWalletBalanceById(masterWalletId);
        return modelMapper.map(masterWallet, MasterWalletDTO.class);
    }

    // FLUSH 된 트랜잭션 조회
    public FlushedTxDTO findFlushedTxByTxId(String txId){
        FlushedTx flushedTx = walletService.findFlushedTxByTxId(txId);
        return modelMapper.map(flushedTx, FlushedTxDTO.class);
    }

    public UserWalletDTO findUserWalletByWalletId(String walletId){
        UserWallet userWallet = walletService.findUserWalletByWalletId(walletId);
        return modelMapper.map(userWallet, UserWalletDTO.class);
    }






}
