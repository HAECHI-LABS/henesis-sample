package io.haechi.henesis.assignment.domain;


public interface ExchangeService {
    UserWallet createUserWallet(String walletName);
    UserWallet findUserWalletByWalletId(String walletId);

    MasterWalletBalance findMasterWalletBalanceById(String masterWalletId);
    FlushedTx findFlushedTxByTxId(String txId);
}
