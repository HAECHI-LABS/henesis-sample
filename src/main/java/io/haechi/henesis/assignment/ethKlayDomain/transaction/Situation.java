package io.haechi.henesis.assignment.ethKlayDomain.transaction;

public enum Situation {

    DEPOSIT_CONFIRMED("deposit_confirmed"),
    ROLLBACK("rollback"),
    UPDATE_STATUS("updated_status");

    Situation(String situation){
    }
}
