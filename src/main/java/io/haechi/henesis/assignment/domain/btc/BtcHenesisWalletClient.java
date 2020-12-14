package io.haechi.henesis.assignment.domain.btc;


import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.Transaction;

import java.util.List;

public interface BtcHenesisWalletClient {


    DepositAddress createDepositAddress(String name);

    Amount getWalletBalance();

    Wallet getWalletInfo();

    Transaction transfer(Amount amount, String to);

    List<BtcTransaction> getTransfers(String updatedAt);

    List<DepositAddress> getAllDepositAddress();

    List<DepositAddress> getAllWallet();

}
