package io.haechi.henesis.assignment.domain.util;

import java.math.BigInteger;

public class Converter {
    public static String add0x(String hexString) {
        if (hexString.length() > 2 && hexString.substring(0, 2).equals("0x")) {
            return hexString;
        }
        return "0x" + hexString;
    }

    public static String remove0x(String hexString) {
        if (hexString.length() > 2 && hexString.substring(0, 2).equals("0x")) {
            return hexString.substring(2);
        }
        return hexString;
    }

    public static String byteArrayToHexString(byte[] byteArray) {

        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(String.format("%02x", b));
        }
        return "0x" + sb.toString();
    }

    public static byte[] hexStringToByteArray(String hexString) {
        String target = hexString;
        if (target == null) {
            return null;
        }
        target = remove0x(target);
        if (target.length() == 0) {
            return null;
        }

        byte[] ba = new byte[target.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(target.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

    public static BigInteger hexStringToBigInteger(String hexString) {
        if (!hexString.startsWith("0x")) {
            throw new IllegalArgumentException(String.format("invalid hex string format: %s", hexString));
        }

        return new BigInteger(remove0x(hexString), 16);
    }

    public static String bigIntegerToHexString(BigInteger bigInteger) {
        return Converter.add0x(bigInteger.toString(16));
    }
}
