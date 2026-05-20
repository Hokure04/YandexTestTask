package org.example.Utils;

import java.util.Arrays;

public final class CompareImagesUtil {

    public static boolean areImagesEqual(byte[] actualImageBytes, byte[] expectedImageBytes) {
        return Arrays.equals(actualImageBytes, expectedImageBytes);
    }
}