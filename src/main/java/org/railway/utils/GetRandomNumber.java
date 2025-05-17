package org.railway.utils;

import java.util.Random;

public class GetRandomNumber {
    public static String generateVerificationCode(int length) {
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1);
        return String.valueOf(random.nextInt(9 * min) + min);
    }
}
