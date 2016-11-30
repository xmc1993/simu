package cn.superid.utils;

import javax.mail.internet.InternetAddress;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXY0123456789";
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

    public static boolean isEmail(String str){

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        Matcher matcher = pattern.matcher(str);
        return  matcher.matches();
    }

    public static boolean isMobile(String str){
        Pattern pattern = null;
        String[] strs = str.split("\\s+");
        Matcher matcher;
        if(strs.length==1){
            pattern = Pattern.compile("^((\\+?86)|(\\(\\+86\\)))?1\\d{10}$");
            matcher = pattern.matcher(str);
        }
        else {
            if(strs[0].equals("+86")){
                pattern = Pattern.compile("^((\\+?86)|(\\(\\+86\\)))?1\\d{10}$");
            }else {
                pattern = Pattern.compile("^\\d+$");
            }
            matcher = pattern.matcher(strs[1]);
        }


        return matcher.matches();
    }

}
