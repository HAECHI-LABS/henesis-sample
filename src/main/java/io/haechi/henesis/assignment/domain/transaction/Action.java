package io.haechi.henesis.assignment.domain.transaction;

public interface Action {

    void updateBalanceBy(Transaction transaction);
}
