package io.haechi.henesis.assignment.domain.transaction;

import io.haechi.henesis.assignment.domain.UserWallet;
import io.haechi.henesis.assignment.domain.UserWalletRepository;
import org.springframework.stereotype.Service;


@Service
public class UpdateBalanceAction implements Action {

    private final UserWalletRepository userWalletRepository;

    public UpdateBalanceAction(UserWalletRepository userWalletRepository){
        this.userWalletRepository = userWalletRepository;
    }


    @Override
    public void doAction(Transaction transaction) {
        UserWallet userWallet =  userWalletRepository.findByWalletId(transaction.getWalletId()).orElseThrow(
                ()->new IllegalArgumentException(String.format("Can Not Found UserWallet '%s'",transaction.getWalletId())));

        userWallet.increaseBalanceBy(transaction.getAmount());
        userWalletRepository.save(userWallet);
    }
}
