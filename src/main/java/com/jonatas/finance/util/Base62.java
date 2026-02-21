package com.jonatas.finance.util;

import java.math.BigInteger;

/*
 * Base62 encoding for binary data.
 * Inspired by: https://base62.org/java_sample/
 * Reference:  https://base62.org/pt/
 */
public final class Base62 {

    private static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int POSITIVE_SIGNED = 1;
    private static final BigInteger BASE_VALUE = BigInteger.valueOf(62);
    private static final double LOG2_62 = 5.9541; // log2(62) ≃ 5.9541, used in encode result for estimate Base62 string min-length

    private Base62() {
    }

    public static String encode(byte[] input) {
        if (input.length == 0) {
            return "";
        }
        int capacity = (int) Math.ceil(input.length * 8 / LOG2_62);
        StringBuilder result = new StringBuilder(capacity);

        BigInteger[] divMod;
        BigInteger value = new BigInteger(POSITIVE_SIGNED, input);
        while (!value.equals(BigInteger.ZERO)) {
            divMod = value.divideAndRemainder(BASE_VALUE);
            result.append(BASE62_ALPHABET.charAt(divMod[1].intValue()));
            value = divMod[0];
        }

        /* find leading zeros in byte[] */
        for (byte b : input) {
            if (b == 0) {
                result.append(BASE62_ALPHABET.charAt(0));
            } else {
                break;
            }
        }
        return result.reverse().toString();
    }

}
