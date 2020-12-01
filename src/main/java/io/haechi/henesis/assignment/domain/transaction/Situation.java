package io.haechi.henesis.assignment.domain.transaction;

public enum Situation {

    DEPOSIT_CONFIRMED("deposit_confirmed"),
    ROLLBACK("rollback"),
    UPDATE_STATUS("updated_status");

    Situation(String situation){
    }
}
