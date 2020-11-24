package io.haechi.henesis.assignment.domain.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Converter {



    public static Double hexStringToDouble(String amount){
        return new BigInteger(amount.substring(2), 16).doubleValue()/Math.pow(10, 18);
    }

    public static String DoubleToHexString(Double amount){
        return "0x"+BigDecimal.valueOf(amount* Math.pow(10, 18)).toBigInteger().toString(16);
    }


/*
    public static String bigIntegerToHexString(Double value) {
        return Converter.add0x(value.toString(16));
    }

    public static Double hexStringToBigInteger(String hexString) {
        if (!hexString.startsWith("0x")) {
            throw new IllegalArgumentException(String.format("invalid hex string format: %s", hexString));
        }
        return new Double(remove0x(hexString), 16)/Double.valueOf(10).doubleValue());
    }
    */

}
