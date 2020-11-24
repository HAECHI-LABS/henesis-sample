package io.haechi.henesis.assignment.domain;


import io.haechi.henesis.assignment.domain.arguments.CreateUserArguments;
import io.haechi.henesis.assignment.domain.arguments.TransferArguments;

public interface ExchangeService {

    UserWallet findUserWalletByWalletId(String walletId);

    MasterWalletBalance findMasterWalletBalanceById(String masterWalletId);
    FlushedTx findFlushedTxByTxId(String txId);

    UserWallet createUserWallet(CreateUserArguments createWalletDTO);

    //Transaction transfer(TransferArguments transferDTO, String userWalletId);
    Transaction transfer(String userWalletId, String amount, String to, String ticker, String passphrase);
}
