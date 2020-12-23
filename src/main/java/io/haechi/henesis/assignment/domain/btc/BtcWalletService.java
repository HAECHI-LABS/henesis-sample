package io.haechi.henesis.assignment.domain.btc;

import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.ethklay.Amount;

import java.util.List;

public interface BtcWalletService {


    DepositAddress createDepositAddress(String name);

    Amount getWalletBalance();

    void transfer(Amount amount, String to);

    Amount getEstimatedFee();

    List<Transfer> getTransactions(String updatedAtGte);
}
