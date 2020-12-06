package io.haechi.henesis.assignment.domain.transaction;

import io.haechi.henesis.assignment.domain.FlushedTxRepository;
import io.haechi.henesis.assignment.domain.UserWalletRepository;
import io.haechi.henesis.assignment.domain.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceUpdater implements Action {

    private final UserWalletRepository userWalletRepository;
    private final TransactionRepository transactionRepository;
    private final FlushedTxRepository flushedTxRepository;

    public BalanceUpdater(UserWalletRepository userWalletRepository,
                          TransactionRepository transactionRepository,
                          FlushedTxRepository flushedTxRepository){
        this.userWalletRepository = userWalletRepository;
        this.transactionRepository = transactionRepository;
        this.flushedTxRepository = flushedTxRepository;
    }


    @Override
    public void doAction(Transaction transaction) {


        // check duplicated Transaction
        if (transactionRepository.findTransactionByDetailId(transaction.getDetailId()).isPresent()){ return; }
        if (flushedTxRepository.findByTxId(transaction.getTransactionId()).isPresent()){return;}

        Wallet wallet =  userWalletRepository.findByWalletId(transaction.getWalletId()).orElseThrow(
                ()->new IllegalArgumentException(String.format("Can Not Found UserWallet '%s','%s'",transaction.getWalletName(), transaction.getWalletId())));


        wallet.increaseBalanceBy(transaction.getAmount());
        userWalletRepository.updateWalletBalance(wallet.getBalance(), wallet.getWalletId());
        log.info(String.format("%s : Update Balance..!",transaction.situation()));

    }
}
