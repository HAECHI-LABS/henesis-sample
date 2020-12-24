package io.haechi.henesis.assignment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.BigInteger;


@Embeddable
@Getter
@NoArgsConstructor
public class Amount {
    private BigInteger value;

    private Amount(String value) {
        this.value = new BigInteger(value.substring(2), 16);
    }

    private Amount(BigInteger value) {
        this.value = value;
    }

    private Amount(Amount value) {
        this.value = value.value;
    }

    // TODO: decimal
    private Amount(Double value) {
        this.value = BigDecimal.valueOf(value * Math.pow(10, 18)).toBigInteger();
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
        return "0x" + this.value.toString(16);
    }

    public Double toDouble() {
        return this.value.doubleValue() / (BigInteger.TEN.pow(18)).doubleValue();
    }

    public Amount add(Amount amount) {
        this.value = this.value.add(amount.getValue());
        return this;
    }

    public boolean isSpendableAmount(Amount amount) {
        try {
            this.subtract(amount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Amount subtract(Amount amount) {
        if (this.compareTo(amount) < 0) {
            throw new IllegalStateException("amount cannot be negative");
        }
        this.value = this.value.subtract(amount.getValue());
        return this;
    }

    public int compareTo(Amount amount) {
        return this.value.compareTo(amount.getValue());
    }

    public void subtractBy(Amount value, Amount spendableAmount) {

        if (this.value.compareTo(value.value) < 0) {
            throw new IllegalStateException("Not Enough User Wallet Balance..!");
        }
        if (spendableAmount.value.compareTo(value.value) < 0) {
            throw new IllegalStateException("Not Enough Master Wallet Balance..!");
        }

        this.value = this.value.subtract(value.value);
    }

    // btc
    public void subtractBy(Amount value, Amount feeAmount, Amount spendableAmount) {
        // 수수료 포함 금액
        value.add(feeAmount);

        if (this.value.compareTo(value.value) < 0) {
            throw new IllegalStateException("Not Enough Deposit Address Balance..!");
        }
        if (spendableAmount.value.compareTo(value.value) < 0) {
            throw new IllegalStateException("Not Enough Wallet Balance..!");
        }
        this.value = this.value.subtract(value.value);
    }


    public void mul(Amount value) {
        if (this.value.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalStateException(
                    "cannot multiply by negative value."
            );
        }
        this.value = this.value.multiply(value.value);
    }

    public void div(Amount value) {
        if (this.value.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalStateException(
                    "cannot divide by non-positive value."
            );
        }
        this.value = this.value.divide(value.value);
    }

    public int isLessThen(Amount value) {

        return this.value.compareTo(value.value);
    }


}
