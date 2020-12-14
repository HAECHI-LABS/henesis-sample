package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import io.haechi.henesis.assignment.domain.ethklay.Wallet;
import io.haechi.henesis.assignment.domain.ethklay.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceUpdateAction implements UpdateAction {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;

    public BalanceUpdateAction(WalletRepository walletRepository,
                               TransactionRepository transactionRepository,
                               FlushedTransactionRepository flushedTransactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
    }


    @Override
    public void doAction(Transaction transaction) {

        Wallet wallet = walletRepository.findByWalletId(transaction.getWalletId()).orElseThrow(
                () -> new IllegalArgumentException(String.format("Can Not Found UserWallet '%s','%s'", transaction.getWalletName(), transaction.getWalletId())));

        // check duplicated Transaction

        if (transactionRepository.existsByTransactionIdAndStatus(
                transaction.getTransactionId(),
                transaction.getStatus())
                || flushedTransactionRepository.existsByTransactionIdAndStatus(
                transaction.getTransactionId(),
                transaction.getStatus())) {
            return;
        }

        wallet.increaseBalanceBy(transaction.getAmount());
        walletRepository.save(wallet);


        //walletRepository.updateWalletBalance(wallet.getBalance(), wallet.getWalletId());
        log.info(String.format("%s : Update Balance..!", transaction.situation()));

    }
}
