package io.haechi.henesis.assignment.domain;


import io.haechi.henesis.assignment.application.dto.CreateUserWalletDTO;
import io.haechi.henesis.assignment.application.dto.TransferDTO;

public interface ExchangeService {

    UserWallet findUserWalletByWalletId(String walletId);

    MasterWalletBalance findMasterWalletBalanceById(String masterWalletId);
    FlushedTx findFlushedTxByTxId(String txId);

    UserWallet createUserWallet(CreateUserWalletDTO createUserWalletDTO);
    Transaction transferCoin(TransferDTO transferDTO);
}
