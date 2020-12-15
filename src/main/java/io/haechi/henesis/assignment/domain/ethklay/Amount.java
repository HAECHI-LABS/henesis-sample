package io.haechi.henesis.assignment.domain.ethklay;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.BigInteger;


@Embeddable
@NoArgsConstructor
public class Amount {
    private BigInteger amount;

    private Amount(String value) {
        this.amount = new BigInteger(value.substring(2), 16);
    }

    private Amount(BigInteger value) {
        this.amount = value;
    }

    private Amount(Amount value) {
        this.amount = value.amount;
    }

    private Amount(Double amount) {
        this.amount = BigDecimal.valueOf(amount * Math.pow(10, 18)).toBigInteger();
    }

    public static Amount of(String value) {
        return new Amount(value);
    }

    public static Amount of(BigInteger value) {
        return new Amount(value);
    }

    public static Amount of(Amount value) {
        return new Amount(value);
    }

    public static Amount of(Double amount) {
        return new Amount(amount);
    }

    public String toHexString() {
        return "0x" + this.amount.toString(16);
    }

    public Double toDouble() {
        return this.amount.doubleValue() / (BigInteger.TEN.pow(18)).doubleValue();
    }

    public void add(Amount value) {
        this.amount = this.amount.add(value.amount);
    }


    public void withdrawBy(Amount value, Amount balance, Amount spendableAmount) {
        if (this.amount.compareTo(value.amount) < 0) {
            throw new IllegalStateException(
                    "balance cannot be negative."
            );
        }
        // 출금 가능 여부 판단
        if (balance.isLessThen(value) < 0) {
            throw new IllegalStateException("Not Enough User Wallet Balance..!");
        }
        if (spendableAmount.isLessThen(balance) < 0) {
            throw new IllegalStateException("Not Enough Master Wallet Balance..!");
        }

        this.amount = this.amount.subtract(value.amount);
    }


    public void mul(Amount value) {
        if (amount.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalStateException(
                    "cannot multiply by negative value."
            );
        }
        this.amount = this.amount.multiply(value.amount);
    }

    public void div(Amount value) {
        if (amount.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalStateException(
                    "cannot divide by non-positive value."
            );
        }
        this.amount = this.amount.divide(value.amount);
    }

    public int isLessThen(Amount value) {

        return this.amount.compareTo(value.amount);
    }


}
