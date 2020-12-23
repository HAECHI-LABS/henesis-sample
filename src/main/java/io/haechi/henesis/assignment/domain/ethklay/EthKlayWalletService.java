package io.haechi.henesis.assignment.domain.ethklay;


import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.Transfer;

import java.util.List;

public interface EthKlayWalletService {

    DepositAddress createDepositAddress(String walletName);

    Amount getMasterWalletBalance();

    Transfer transfer(Amount amount, String to);

    List<String> getDepositAddressIds();

    Transfer flushAll(List<String> userWalletIds);

    TransferEvent getTransactions(String updatedAtGte);

    List<DepositAddress> getAllDepositAddresses();
}
