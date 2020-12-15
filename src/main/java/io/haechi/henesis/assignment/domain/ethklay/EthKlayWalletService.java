package io.haechi.henesis.assignment.domain.ethklay;


import io.haechi.henesis.assignment.domain.Transaction;

import java.util.List;

public interface EthKlayWalletService {

    Wallet createUserWallet(String walletName);

    Amount getMasterWalletBalance();

    Transaction transfer(Amount amount, String to);

    List<String> getUserWalletIds();

    Transaction flushAll(List<String> userWalletIds);

    List<Transaction> getTransactions(String updatedAt);

    List<Wallet> getAllUserWallet();

    List<Wallet> getAllMasterWallet();

}
