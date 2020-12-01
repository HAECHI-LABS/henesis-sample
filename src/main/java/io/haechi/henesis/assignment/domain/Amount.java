package io.haechi.henesis.assignment.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.BigInteger;


@Embeddable
@NoArgsConstructor
public class Amount {
    private BigInteger value;

    public static Amount of(String amount){
        return new Amount(amount);
    }
    private Amount(String amount){
        this.value = new BigInteger(amount.substring(2), 16);
    }

    public static Amount of(BigInteger amount){return new Amount(amount);}
    private Amount(BigInteger amount){
        this.value = amount;
    }

    public static Amount of(Double amount){return new Amount(amount);}
    private Amount(Double amount){
        this.value = BigDecimal.valueOf(amount*Math.pow(10,18)).toBigInteger();
    }

    public String toHexString() {
        return "0x"+this.value.toString(16);
    }
    public Double toDouble(){
        return this.value.divide(BigInteger.TEN.pow(18)).doubleValue();
    }

    public void changeAmount(BigInteger amount) {
        this.value = amount;
    }


    public void add(Amount amount) {
        this.value = this.value.add(amount.value);
    }

    public void subtract(Amount amount) {
        if (this.value.compareTo(amount.value) < 0) {
            throw new IllegalStateException(
                    "balance cannot be negative."
            );
        }
        this.value = this.value.subtract(amount.value);
    }

    public void mul(Amount amount) {
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalStateException(
                    "cannot multiply by negative value."
            );
        }
        this.value = this.value.multiply(amount.value);
    }

    public void div(Amount amount) {
        if (value.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalStateException(
                    "cannot divide by non-positive value."
            );
        }
        this.value = this.value.divide(amount.value);
    }

    public int compareTo(Amount amount) {

        return this.value.compareTo(amount.value);
    }






}
