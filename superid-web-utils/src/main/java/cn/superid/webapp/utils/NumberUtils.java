package cn.superid.webapp.utils;

import java.util.Random;

/**
 * Created by zoowii on 15/4/12.
 */
public class NumberUtils {
    private static final Random rand = new Random();

    public static String randomNumberString(int length) {
        String[] numberChars = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<length;++i) {
            int index = rand.nextInt(numberChars.length);
            if(index>=numberChars.length) {
                index = numberChars.length - 1;
            }
            builder.append(numberChars[index]);
        }
        return builder.toString();
    }

    public static int tryParseInt(Object obj, int defaultValue) {
        if(obj == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static int tryParseInt(Object obj) {
        return tryParseInt(obj, 0);
    }
    public static long tryParseLong(Object obj, long defaultValue) {
        if(obj == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static long tryParseLong(Object obj) {
        return tryParseLong(obj, 0L);
    }
    public static float tryParseFloat(Object obj, float defaultValue) {
        if(obj == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static float tryParseFloat(Object obj) {
        return tryParseFloat(obj, 0.0F);
    }
    public static double tryParseDouble(Object obj, double defaultValue) {
        if(obj == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static double tryParseDouble(Object obj) {
        return tryParseDouble(obj, 0.0);
    }
}
