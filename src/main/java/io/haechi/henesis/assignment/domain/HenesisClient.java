package io.haechi.henesis.assignment.domain;

import java.util.List;

public interface HenesisClient {
    DepositAddress createDepositAddress(String name);

    Transfer transfer(String to, String symbol, Amount amount);

    Amount getMasterWalletBalance();

    Amount getEstimatedFee();

    List<Transfer> getLatestTransfersByUpdatedAtGte(String updatedAtGte);

    Transfer flush(List<String> depositAddressIds);

    DepositAddress getDepositAddress(String id);

    Coin getCoin(String symbol);
}
