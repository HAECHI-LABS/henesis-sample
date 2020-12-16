package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.ethklay.Wallet;
import io.haechi.henesis.assignment.domain.ethklay.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceUpdateAction implements UpdateAction {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public BalanceUpdateAction(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public void doAction(Transaction transaction) {

        Wallet wallet = walletRepository.findByWalletId(transaction.getWalletId()).orElseThrow(
                () -> new IllegalArgumentException(String.format("Can Not Found UserWallet '%s','%s'", transaction.getWalletName(), transaction.getWalletId())));

        try {
            wallet.increaseBalanceBy(transaction.getAmount());
            walletRepository.save(wallet);
            log.info(String.format("%s : Update Balance..! (%s)", transaction.situation(),transaction.getWalletName()));
        } catch (Exception e) {
            log.info("ERROR : Fail To Update User Wallet Balance");
        }
    }
}
