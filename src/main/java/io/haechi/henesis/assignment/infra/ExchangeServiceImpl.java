package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.ExchangeService;
import io.haechi.henesis.assignment.domain.FlushedTx;
import io.haechi.henesis.assignment.domain.MasterWallet;
import io.haechi.henesis.assignment.domain.UserWallet;
import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final HenesisWalletService henesisWalletSerivce;

    public ExchangeServiceImpl(HenesisWalletService henesisWalletSerivce){
        this.henesisWalletSerivce = henesisWalletSerivce;
    }


    @Override
    public UserWallet createUserWallet(String walletName){
        return henesisWalletSerivce.createUserWallet(walletName);
    }

    @Override
    public UserWallet findUserWalletByWalletId(String walletId) {
        return null;
    }

    @Override
    public MasterWallet findMasterWalletBalanceById(String masterWalletId) {
        return null;
    }

    @Override
    public FlushedTx findFlushedTxByTxId(String txId) {
        return null;
    }
}
