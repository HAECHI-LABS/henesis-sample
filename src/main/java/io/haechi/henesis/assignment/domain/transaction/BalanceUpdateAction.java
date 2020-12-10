package io.haechi.henesis.assignment.domain.transaction;

import io.haechi.henesis.assignment.domain.FlushedTransactionRepository;
import io.haechi.henesis.assignment.domain.UserWalletRepository;
import io.haechi.henesis.assignment.domain.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceUpdateAction implements UpdateAction {

    private final UserWalletRepository userWalletRepository;
    private final TransactionRepository transactionRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;

    public BalanceUpdateAction(UserWalletRepository userWalletRepository,
                               TransactionRepository transactionRepository,
                               FlushedTransactionRepository flushedTransactionRepository){
        this.userWalletRepository = userWalletRepository;
        this.transactionRepository = transactionRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
    }


    @Override
    public void doAction(Transaction transaction) {

        Wallet wallet =  userWalletRepository.findByWalletId(transaction.getWalletId()).orElseThrow(
                ()->new IllegalArgumentException(String.format("Can Not Found UserWallet '%s','%s'",transaction.getWalletName(), transaction.getWalletId())));

        // check duplicated Transaction
        if (transactionRepository.findTransactionByDetailId(transaction.getDetailId()).isPresent()){ return; }
        if (flushedTransactionRepository.findByTxId(transaction.getTransactionId()).isPresent()){return;}




        wallet.increaseBalanceBy(transaction.getAmount());
        userWalletRepository.updateWalletBalance(wallet.getBalance(), wallet.getWalletId());
        log.info(String.format("%s : Update Balance..!",transaction.situation()));

    }
}
