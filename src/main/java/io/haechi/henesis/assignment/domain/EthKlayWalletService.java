package io.haechi.henesis.assignment.domain;


import java.util.List;

public interface EthKlayWalletService {

    DepositAddress createDepositAddress(String walletName);

    Transfer transfer(String to, Amount amount);

    List<String> getDepositAddressIds();

    Transfer flushAll(List<String> userWalletIds);

    List<Transfer> getLatestTransfersByUpdatedAtGte(String updatedAtGte);

    DepositAddress getDepositAddress(String id);
}
