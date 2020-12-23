package io.haechi.henesis.assignment.domain.ethklay;


import io.haechi.henesis.assignment.domain.DepositAddress;

import java.util.List;

public interface EthKlayWalletService {

    DepositAddress createDepositAddress(String walletName);

    Amount getMasterWalletBalance();

    Transaction transfer(Amount amount, String to);

    List<String> getUserWalletIds();

    Transaction flushAll(List<String> userWalletIds);

    TransferEvent getTransactions(String updatedAtGte);

    List<DepositAddress> getAllDepositAddresses();
}
