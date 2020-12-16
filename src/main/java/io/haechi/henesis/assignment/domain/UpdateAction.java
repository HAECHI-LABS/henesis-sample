package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.btc.BtcTransaction;
import io.haechi.henesis.assignment.domain.ethklay.Transaction;

public interface UpdateAction {

    void updateBalance(Transaction transaction);

    void updateBalance(BtcTransaction btcTransaction);
}
