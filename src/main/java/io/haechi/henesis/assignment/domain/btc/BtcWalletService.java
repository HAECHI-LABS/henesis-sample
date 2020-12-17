package io.haechi.henesis.assignment.domain.btc;

import java.util.List;

public interface BtcWalletService {


    DepositAddress createDepositAddress(String name);

    BtcAmount getWalletBalance();

    void transfer(BtcAmount amount, String to);

    BtcAmount getEstimatedFee();

    List<BtcTransaction> getTransactions(String updatedAtGte);
}
