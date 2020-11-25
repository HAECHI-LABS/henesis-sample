package io.haechi.henesis.assignment.domain;


import io.haechi.henesis.assignment.domain.arguments.CreateUserArguments;

public interface ExchangeService {

    UserWallet createUserWallet(CreateUserArguments createUserArguments);

    Transaction transfer(String userWalletId, String amount, String to, String ticker, String passphrase);

    Transaction flush(String ticker, String passphrase);
}
