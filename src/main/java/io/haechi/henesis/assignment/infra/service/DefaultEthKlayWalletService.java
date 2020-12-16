package io.haechi.henesis.assignment.infra.service;

import io.haechi.henesis.assignment.domain.ethklay.Amount;
import io.haechi.henesis.assignment.domain.ethklay.EthKlayWalletService;
import io.haechi.henesis.assignment.domain.ethklay.Transaction;
import io.haechi.henesis.assignment.domain.ethklay.Wallet;
import io.haechi.henesis.assignment.domain.exception.ErrorCode;
import io.haechi.henesis.assignment.domain.exception.InternalServerException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultEthKlayWalletService implements EthKlayWalletService {

    @Override
    public Wallet createUserWallet(String walletName) {

        return this.walletServiceCallTemplate(() -> this.createUserWallet(walletName));
    }

    @Override
    public Amount getMasterWalletBalance() {
        return this.walletServiceCallTemplate(this::getMasterWalletBalance);
    }

    @Override
    public Transaction transfer(Amount amount, String to) {
        return this.walletServiceCallTemplate(() -> this.transfer(amount, to));
    }

    @Override
    public List<String> getUserWalletIds() {
        return this.walletServiceCallTemplate(this::getUserWalletIds);
    }

    @Override
    public Transaction flushAll(List<String> userWalletIds) {
        return this.walletServiceCallTemplate(() -> this.flushAll(userWalletIds));
    }

    @Override
    public List<Transaction> getTransactions(String updatedAt) {
        return this.walletServiceCallTemplate(() -> this.getTransactions(updatedAt));
    }

    @Override
    public List<Wallet> getAllUserWallet() {
        return this.walletServiceCallTemplate(this::getAllUserWallet);
    }

    @Override
    public List<Wallet> getAllMasterWallet() {
        return this.walletServiceCallTemplate(this::getAllMasterWallet);
    }

    protected <T> T walletServiceCallTemplate(WalletServiceRequestCallBack<T> callback) {
        try {
            return callback.send();
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage(), ErrorCode.INTERNAL_SERVER);
        }
    }

    private interface WalletServiceRequestCallBack<T> {
        T send() throws Exception;
    }

}
