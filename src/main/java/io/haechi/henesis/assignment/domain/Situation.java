package io.haechi.henesis.assignment.domain;

public enum Situation {

    DEPOSIT_BTC("deposit_btc"),
    ROLLBACK_BTC("rollback_btc"),

    DEPOSIT_ETH_KLAY("deposit_eth_klay"),
    ROLLBACK_ETH_KLAY("rollback_eth_klay"),

    NOTHING_TO_DO("nothing_to_do");

    Situation(String situation) {
    }
}
