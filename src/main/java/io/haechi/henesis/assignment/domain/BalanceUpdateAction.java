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

    public BalanceUpdateAction(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @Override
    public void doAction(Transaction transaction) {

        Wallet wallet = walletRepository.findByWalletId(transaction.getWalletId()).orElseThrow(
                () -> new IllegalArgumentException(String.format("Can Not Found UserWallet '%s','%s'", transaction.getWalletName(), transaction.getWalletId())));

        try{
            wallet.increaseBalanceBy(transaction.getAmount());
            walletRepository.save(wallet);
        }catch (Exception e){
            log.info("ERROR : Fail To Update User Wallet Balance");
        }

        log.info(String.format("%s : Update Balance..!", transaction.situation()));

    }
}
