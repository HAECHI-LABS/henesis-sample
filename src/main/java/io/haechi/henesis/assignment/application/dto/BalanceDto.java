package io.haechi.henesis.assignment.application.dto;

import io.haechi.henesis.assignment.domain.Balance;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceDto {
    private String symbol;
    private BigInteger amount;

    public BalanceDto(Balance balance) {
        this.symbol = balance.getSymbol();
        this.amount = balance.getAmount().getValue();
    }
}
