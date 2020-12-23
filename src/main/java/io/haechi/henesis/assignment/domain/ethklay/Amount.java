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

    // TODO: decimal
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


    public void subtractBy(Amount value, Amount spendableAmount) {

        if (this.amount.compareTo(value.amount) < 0) {
            throw new IllegalStateException("Not Enough User Wallet Balance..!");
        }
        if (spendableAmount.amount.compareTo(value.amount) < 0) {
            throw new IllegalStateException("Not Enough Master Wallet Balance..!");
        }

        this.amount = this.amount.subtract(value.amount);
    }

    // btc
    public void subtractBy(Amount value, Amount feeAmount, Amount spendableAmount) {
        // 수수료 포함 금액
        value.add(feeAmount);

        if (this.amount.compareTo(value.amount) < 0) {
            throw new IllegalStateException("Not Enough Deposit Address Balance..!");
        }
        if (spendableAmount.amount.compareTo(value.amount) < 0) {
            throw new IllegalStateException("Not Enough Wallet Balance..!");
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
