package io.haechi.henesis.assignment.domain;

public interface WalletService {
    UserWallet createUserWallet(String masterWalletId);
    MasterWallet findMasterWalletBalanceById(String masterWalletId);
    FlushedTx findFlushedTxByTxId(String txId);
    UserWallet findUserWalletByWalletId(String walletId);
}
