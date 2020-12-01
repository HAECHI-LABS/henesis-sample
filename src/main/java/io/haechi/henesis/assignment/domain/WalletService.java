package io.haechi.henesis.assignment.domain;


import io.haechi.henesis.assignment.domain.transaction.Transaction;

import java.util.List;

public interface WalletService {

    UserWallet createUserWallet(String walletName);

    Amount getMasterWalletBalance(String ticker);

    Transaction transfer(Amount amount, String to, String ticker);

    List<String> getUserWalletIds();

    Transaction flushAll(String ticker, List<String> userWalletIds);

    List<Transaction> getValueTransferEvents();

}
