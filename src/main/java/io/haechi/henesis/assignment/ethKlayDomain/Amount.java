package io.haechi.henesis.assignment.ethKlayDomain;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.BigInteger;


@Embeddable
@NoArgsConstructor
public class Amount {
    private BigInteger amount;

    public static Amount of(String value){
        return new Amount(value);
    }
    private Amount(String value){
        this.amount = new BigInteger(value.substring(2), 16);
    }

    public static Amount of(BigInteger value){return new Amount(value);}
    private Amount(BigInteger value){
        this.amount = value;
    }

    public static Amount of(Amount value){return new Amount(value);}
    private Amount(Amount value){this.amount=value.amount;}


    public static Amount of(Double amount){return new Amount(amount);}
    private Amount(Double amount){
        this.amount = BigDecimal.valueOf(amount*Math.pow(10,18)).toBigInteger();
    }

    public String toHexString() {
        return "0x"+this.amount.toString(16);
    }

    public Double toDouble(){
        return this.amount.doubleValue()/(BigInteger.TEN.pow(18)).doubleValue();
    }

    public void add(Amount value) {
        this.amount = this.amount.add(value.amount);
    }

    public void subtract(Amount value) {
        if (this.amount.compareTo(value.amount) < 0) {
            throw new IllegalStateException(
                    "balance cannot be negative."
            );
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

    public int compareTo(Amount value) {

        return this.amount.compareTo(value.amount);
    }






}
