package io.haechi.henesis.assignment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
public class CoinBalance {

    private BigInteger amount;
    private BigInteger spendableAmount;


    public CoinBalance(BigInteger balance, BigInteger amount){
        this.amount = amount;
    }

    public void add(BigInteger amount){
        this.amount = this.amount.add(amount);
    }

    public BigInteger subtract(BigInteger left, BigInteger right){
        if(left.compareTo(right)<0){
            throw new IllegalStateException(String.format("amount cannot be negative. remain amount: '%s', requested amount: '%s'", left, right));
        }
        return left.subtract(right);
    }


}
