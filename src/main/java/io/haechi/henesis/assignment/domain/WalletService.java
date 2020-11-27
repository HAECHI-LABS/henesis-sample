package io.haechi.henesis.assignment.domain;


import java.math.BigInteger;
import java.util.List;

public interface WalletService {

    UserWallet createUserWallet(String walletName);

    Amount getMasterWalletBalance(String ticker);
    Transaction transfer(BigInteger amount, String to, String ticker);

    List<String> getUserWalletIds();
    Transaction flushAll(String ticker, List<String> userWalletIds);

}
