package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.ExchangeService;
import io.haechi.henesis.assignment.domain.FlushedTx;
import io.haechi.henesis.assignment.domain.MasterWalletBalance;
import io.haechi.henesis.assignment.domain.UserWallet;
import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final HenesisWalletService henesisWalletService;

    public ExchangeServiceImpl(HenesisWalletService henesisWalletService){
        this.henesisWalletService = henesisWalletService;
    }


    @Override
    public UserWallet createUserWallet(String walletName){
        return henesisWalletService.createUserWallet(walletName);
    }

    @Override
    public UserWallet findUserWalletByWalletId(String walletId) {
        return null;
    }

    @Override
    public MasterWalletBalance findMasterWalletBalanceById(String masterWalletId) {
        return null;
    }

    @Override
    public FlushedTx findFlushedTxByTxId(String txId) {
        return null;
    }
}
