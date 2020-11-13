package io.haechi.henesis.assignment.domain;

import java.util.List;

public interface BtcWalletService {
    DepositAddress createDepositAddress(String name);

    Amount getWalletBalance();

    Transfer transfer(Amount amount, String to);

    Amount getEstimatedFee();

    List<Transfer> getLatestTransfersByUpdatedAtGte(String updatedAtGte);
}
