package io.haechi.henesis.assignment.domain;

public enum Situation {

    DEPOSIT_CONFIRMED("deposit_confirmed"),
    ROLLBACK("rollback"),
    WITHDRAWAL_CONFIRMED("withdrawal_confirmed");

    Situation(String situation) {
    }
}
