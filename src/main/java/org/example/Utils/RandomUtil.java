package org.example.Utils;

import java.util.Random;

public final class RandomUtil {

    private static final Random RANDOM = new Random();

    public static int generateNumber(int min, int max){
        if(min > max){
            throw new IllegalArgumentException("Значение min не может быть больше max");
        }
        return RANDOM.nextInt(min, max);
    }

    public static String getRandomNumberAsString(int min, int max){
        return String.valueOf(generateNumber(min, max));
    }
}
