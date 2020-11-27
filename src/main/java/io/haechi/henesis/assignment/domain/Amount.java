package io.haechi.henesis.assignment.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.math.BigInteger;


@Embeddable
@NoArgsConstructor
public class Amount {
    private BigInteger amount;


    public Amount(BigInteger amount) {
        this.amount = amount;
    }

    public Amount(String amount){
        this.amount = new BigInteger(amount.substring(2), 16);
    }

    public static Amount of(String amount){
        return new Amount(amount);
    }


    public void changeAmount(BigInteger amount) {
        this.amount = amount;
    }

    public void add(BigInteger amount) {
        this.amount = this.amount.add(amount);
    }

    public void subtract(BigInteger amount) {
        if (this.amount.compareTo(amount) < 0) {
            throw new IllegalStateException(
                    "balance cannot be negative."
            );
        }
        this.amount = this.amount.subtract(amount);
    }

    public void mul(BigInteger amount) {
        if (amount.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalStateException(
                    "cannot multiply by negative value."
            );
        }
        this.amount = this.amount.multiply(amount);
    }

    public void div(BigInteger amount) {
        if (amount.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalStateException(
                    "cannot divide by non-positive value."
            );
        }
        this.amount = this.amount.divide(amount);
    }

    public int compareTo(BigInteger amount) {
        return this.amount.compareTo(amount);
    }


//    public static String bigIntegerToHexString(BigInteger amount) {
//        return "0x"+amount.toString(16);
//    }



}
