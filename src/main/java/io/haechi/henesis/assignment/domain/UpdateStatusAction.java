package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.btc.BtcTransaction;
import io.haechi.henesis.assignment.domain.ethklay.Transaction;
import org.springframework.stereotype.Service;

@Service
public class UpdateStatusAction implements UpdateAction{
    @Override
    public void updateBalance(Transaction transaction) {
    }

    @Override
    public void updateBalance(BtcTransaction btcTransaction) {

    }
}
