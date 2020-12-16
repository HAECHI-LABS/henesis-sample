package io.haechi.henesis.assignment.domain.btc;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.BigInteger;


@Embeddable
@NoArgsConstructor
public class BtcAmount {
    private BigInteger amount;

    private BtcAmount(String value) {
        this.amount = new BigInteger(value.substring(2), 16);
    }

    private BtcAmount(BigInteger value) {
        this.amount = value;
    }

    private BtcAmount(BtcAmount value) {
        this.amount = value.amount;
    }

    private BtcAmount(Double amount) {
        this.amount = BigDecimal.valueOf(amount * Math.pow(10, 8)).toBigInteger();
    }

    public static BtcAmount of(String value) {
        return new BtcAmount(value);
    }

    public static BtcAmount of(BigInteger value) {
        return new BtcAmount(value);
    }

    public static BtcAmount of(BtcAmount value) {
        return new BtcAmount(value);
    }

    public static BtcAmount of(Double amount) {
        return new BtcAmount(amount);
    }

    public String toHexString() {
        return "0x" + this.amount.toString(16);
    }

    public Double toDouble() {
        return this.amount.doubleValue() / (BigInteger.TEN.pow(8)).doubleValue();
    }

    public void add(BtcAmount value) {
        this.amount = this.amount.add(value.amount);
    }

    public void withdrawBy(BtcAmount value, BtcAmount feeAmount, BtcAmount spendableAmount){
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



    public void subtract(BtcAmount value) {
        if (this.amount.compareTo(value.amount) < 0) {
            throw new IllegalStateException(
                    "balance cannot be negative."
            );
        }
        this.amount = this.amount.subtract(value.amount);
    }

    public void mul(BtcAmount value) {
        if (amount.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalStateException(
                    "cannot multiply by negative value."
            );
        }
        this.amount = this.amount.multiply(value.amount);
    }

    public void div(BtcAmount value) {
        if (amount.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalStateException(
                    "cannot divide by non-positive value."
            );
        }
        this.amount = this.amount.divide(value.amount);
    }

    public int isLessThen(BtcAmount value) {

        return this.amount.compareTo(value.amount);
    }


}
