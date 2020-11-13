package io.haechi.henesis.assignment.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferRequest {
    private String symbol;
    private String to;
    private BigInteger amount;

    public String getSymbol() {
        return this.symbol.toUpperCase();
    }
}
