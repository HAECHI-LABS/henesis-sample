package io.haechi.henesis.assignment.domain;

import java.util.Arrays;

public enum Blockchain {
    ETHEREUM("ethereum", "eth"),
    KLAYTN("klaytn", "klay"),
    BITCOIN("bitcoin", "btc");

    private final String name;
    private final String symbol;

    Blockchain(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public static Blockchain of(String name) {
        return Arrays.stream(values())
                .filter(v -> name.equals(v.name) || name.equalsIgnoreCase(v.name))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("'%s' is not supported blockchain", name)));
    }

    public String toSymbol() {
        return this.symbol;
    }
}
