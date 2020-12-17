package io.haechi.henesis.assignment.domain.ethklay;


import java.util.List;

public interface EthKlayWalletService {

    Wallet createUserWallet(String walletName);

    Amount getMasterWalletBalance();

    Transaction transfer(Amount amount, String to);

    List<String> getUserWalletIds();

    Transaction flushAll(List<String> userWalletIds);

    TransferEvent getTransactions(String updatedAtGte);

    List<Wallet> getAllUserWallet();

    List<Wallet> getAllMasterWallet();

}
