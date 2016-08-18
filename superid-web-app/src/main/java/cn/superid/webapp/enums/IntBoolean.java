package cn.superid.webapp.enums;


public class IntBoolean {
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    public static final Integer TRUE_VALUE = TRUE;
    public static final Integer FALSE_VALUE = FALSE;

    public static int valueOf(boolean boolValue) {
        return boolValue ? TRUE : FALSE;
    }

    public static boolean toBool(int value) {
        return value != FALSE;
    }
}
