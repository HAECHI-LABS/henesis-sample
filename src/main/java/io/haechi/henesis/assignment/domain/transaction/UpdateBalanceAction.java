package io.haechi.henesis.assignment.domain.transaction;

import io.haechi.henesis.assignment.domain.UserWallet;
import io.haechi.henesis.assignment.domain.UserWalletRepository;

public class UpdateBalanceAction implements Action {


    private UserWalletRepository userWalletRepository;

    public UpdateBalanceAction(UserWalletRepository userWalletRepository){
        this.userWalletRepository = userWalletRepository;
    }


    @Override
    public void updateBalanceBy(Transaction tx) {
        UserWallet userWallet =  userWalletRepository.findByWalletId(tx.getWalletId()).orElseThrow(
                ()->new IllegalArgumentException(String.format("Can Not Found UserWallet '%s'",tx.getWalletId())));

        userWallet.increaseBalanceBy(tx.getAmount());
        userWalletRepository.save(userWallet);
    }
}
