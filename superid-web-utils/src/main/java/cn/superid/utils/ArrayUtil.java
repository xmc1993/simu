package cn.superid.utils;

/**
 * Created by jessiechen on 03/01/17.
 */
public class ArrayUtil {
    public static <T> String join(T[] array, String reg) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            T t = array[i];
            sb.append(t.toString());
            if (i < array.length - 1)
                sb.append(reg);
        }
        return sb.toString();
    }
}
