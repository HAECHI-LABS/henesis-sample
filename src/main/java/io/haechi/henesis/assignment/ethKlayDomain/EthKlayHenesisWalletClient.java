package io.haechi.henesis.assignment.ethKlayDomain;


import io.haechi.henesis.assignment.ethKlayDomain.transaction.Transaction;

import java.util.List;

public interface EthKlayHenesisWalletClient {

    Wallet createUserWallet(String walletName);

    Amount getMasterWalletBalance();

    Transaction transfer(Amount amount, String to);

    List<String> getUserWalletIds();

    Transaction flushAll(List<String> userWalletIds);

    List<Transaction> getValueTransferEvents(String updatedAt);

    List<Wallet> getAllUserWallet();

    List<Wallet> getAllMasterWallet();

}
