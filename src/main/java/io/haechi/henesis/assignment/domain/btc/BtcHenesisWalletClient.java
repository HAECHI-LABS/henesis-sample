package io.haechi.henesis.assignment.domain.btc;


import io.haechi.henesis.assignment.domain.Transaction;

import java.util.List;

public interface BtcHenesisWalletClient {


    DepositAddress createDepositAddress(String name);

    BtcAmount getWalletBalance();

    Wallet getWalletInfo();

    Transaction transfer(BtcAmount amount, String to);

    BtcAmount getEstimatedFee();

    List<BtcTransaction> getTransfers(String updatedAt);

    List<DepositAddress> getAllDepositAddress();

    List<DepositAddress> getAllWallet();

}
