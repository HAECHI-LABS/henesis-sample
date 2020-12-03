package io.haechi.henesis.assignment.domain.transaction;

import io.haechi.henesis.assignment.domain.UserWallet;
import io.haechi.henesis.assignment.domain.UserWalletRepository;
import org.springframework.stereotype.Service;


@Service
public class UpdateBalanceAction implements Action {

    private final UserWalletRepository userWalletRepository;
    private final TransactionRepository transactionRepository;

    public UpdateBalanceAction(UserWalletRepository userWalletRepository,
                               TransactionRepository transactionRepository){
        this.userWalletRepository = userWalletRepository;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public void doAction(Transaction transaction) {

        if (transactionRepository.existsTransactionByDetailIdAndStatusAndTransferType(
                transaction.getDetailId(),
                transaction.getStatus(),
                transaction.getTransferType())
        ){
            return;
        }

        UserWallet userWallet =  userWalletRepository.findByWalletId(transaction.getWalletId()).orElseThrow(
                ()->new IllegalArgumentException(String.format("Can Not Found UserWallet '%s','%s'",transaction.getWalletName(), transaction.getWalletId())));

        userWallet.increaseBalanceBy(transaction.getAmount());
        userWalletRepository.save(userWallet);
    }
}
