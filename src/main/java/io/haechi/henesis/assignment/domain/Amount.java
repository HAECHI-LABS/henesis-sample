package io.haechi.henesis.assignment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.math.BigInteger;


@Embeddable
@Getter
@NoArgsConstructor
public class Amount {
    private BigInteger value;

    private Amount(String hexString) {
        if (!hexString.startsWith("0x")) {
            throw new IllegalArgumentException(String.format("invalid hex string format: %s", hexString));
        }
        this.value = new BigInteger(hexString.substring(2), 16);
    }

    private Amount(BigInteger value) {
        this.value = value;
    }

    private Amount(Amount value) {
        this.value = value.value;
    }

    public static Amount of(String value) {
        return new Amount(value);
    }

    public static Amount of(BigInteger value) {
        return new Amount(value);
    }

    public static Amount of(BigInteger value, Integer decimals) {
        return new Amount(value.multiply(BigInteger.TEN.pow(decimals)));
    }

    public static Amount of(Amount value) {
        return new Amount(value);
    }

    public static Amount zero() {
        return new Amount(BigInteger.ZERO);
    }

    public String toHexString() {
        return "0x" + this.value.toString(16);
    }

    public Amount add(Amount amount) {
        return new Amount(this.value.add(amount.getValue()));
    }

    public Amount subtract(Amount amount) {
        return new Amount(this.value.subtract(amount.getValue()));
    }

    public int compareTo(Amount amount) {
        return this.value.compareTo(amount.getValue());
    }
}
