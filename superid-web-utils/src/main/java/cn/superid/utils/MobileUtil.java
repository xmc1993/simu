package cn.superid.utils;

/**
 * Created by njuTms on 16/10/27.
 */
public class MobileUtil {
    private static String[] strs ;

    private static void setStrs(String token){
        strs = token.split("\\s+");
    }
    public static String getMobile(String token){
        setStrs(token);
        return strs[1];
    }

    public static String getCountryCode(String token){
        setStrs(token);
        return strs[0];
    }

    public static boolean isChinaMobile(String token){
        setStrs(token);
        return strs[0].equals("+86") ? true : false;
    }

    public static boolean isValidFormat(String token){
        setStrs(token);
        if(strs.length!=2){
            return false;
        }
        return true;
    }

    public static String toString(String token){
        return token.trim();
    }
}
