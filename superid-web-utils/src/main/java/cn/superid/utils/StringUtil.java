package cn.superid.utils;

import org.omg.CORBA.ORB;

import java.util.List;
import java.util.Random;

/**
 * Created by 维 on 2014/8/29.
 */
public class StringUtil {
    public static boolean isEqualWithTrim(String a, String b) {
        if (a == null) {
            return b == null;
        }
        if (b == null) {
            return false;
        }
        return a.trim().equals(b.trim());
    }

    public static String randomString(int n) {
        if (n < 1) {
            return "";
        }
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String join(List lst, Object sep) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lst.size(); ++i) {
            if (i > 0) {
                builder.append(sep);
            }
            builder.append(lst.get(i));
        }
        return builder.toString();
    }

    public static String join(String[] strArray, Object seq) {
        return join(ListUtil.list(strArray), seq);
    }

    public static String join(int[] lst, Object sep) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lst.length; ++i) {
            if (i > 0) {
                builder.append(sep);
            }
            builder.append(lst[i]);
        }
        return builder.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() < 1;
    }

    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    public static String replaceWithSubStr(String origin, String sub, int index){
        int originSize = origin.length();
        int subSize = sub.length();
        if (originSize - index > subSize){
            return origin.substring(0, index) + sub + origin.substring(index + subSize);
        }else{
            return origin.substring(0, index) + sub;
        }
    }
}
