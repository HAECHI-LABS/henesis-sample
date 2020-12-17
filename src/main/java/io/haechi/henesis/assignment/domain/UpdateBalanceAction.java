package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.btc.BtcTransaction;
import io.haechi.henesis.assignment.domain.btc.DepositAddress;
import io.haechi.henesis.assignment.domain.btc.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.ethklay.Transaction;
import io.haechi.henesis.assignment.domain.ethklay.Wallet;
import io.haechi.henesis.assignment.domain.ethklay.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateBalanceAction implements UpdateAction {

    private final WalletRepository walletRepository;
    private final DepositAddressRepository depositAddressRepository;

    public UpdateBalanceAction(
            WalletRepository walletRepository,
            DepositAddressRepository depositAddressRepository
    ) {
        this.walletRepository = walletRepository;
        this.depositAddressRepository = depositAddressRepository;
    }


    @Override
    public void updateBalance(Transaction transaction) {

        Wallet wallet = walletRepository.findByWalletId(transaction.getWalletId()).orElseThrow(
                () -> new IllegalArgumentException(String.format("Can Not Found UserWallet '%s','%s'", transaction.getWalletName(), transaction.getWalletId())));

        try {
            wallet.increaseBalanceBy(transaction.getAmount());
            walletRepository.save(wallet);
            log.info(String.format("%s : Update Balance..! (%s)", transaction.situation(), transaction.getWalletName()));
        } catch (Exception e) {
            log.info("ERROR : Fail To Update User Wallet Balance");
        }
    }

    @Override
    public void updateBalance(BtcTransaction btcTransaction) {
        DepositAddress depositAddress = depositAddressRepository.findByAddress(btcTransaction.getReceivedAt()).orElseThrow(
                () -> new IllegalArgumentException(String.format("Not Found Deposit Address (%s)..!", btcTransaction.getReceivedAt())));

        try {
            depositAddress.increaseBalanceBy(btcTransaction.getAmount());
            depositAddressRepository.save(depositAddress);
            log.info(String.format("Update Balance..! (%s : %s)", depositAddress.getName(),depositAddress.getBalance().toHexString()));
        } catch (Exception e) {
            log.info("ERROR : Fail To Update Balance");
        }
    }
}
