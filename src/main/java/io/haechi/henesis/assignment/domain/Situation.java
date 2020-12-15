package io.haechi.henesis.assignment.domain;

public enum Situation {

    DEPOSIT_CONFIRMED("deposit_confirmed"),
    ROLLBACK("rollback"),
    NOTHING_TO_DO("nothing_to_do");

    Situation(String situation) {
    }
}
