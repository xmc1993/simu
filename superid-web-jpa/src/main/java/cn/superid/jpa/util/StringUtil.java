package cn.superid.jpa.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() < 1;
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        return (s1 == null && s2 == null) || (s1 != null && s1.equalsIgnoreCase(s2));
    }

    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    public static String randomString(int n) {
        if (n < 1) {
            n = 1;
        }
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int num = random.nextInt(str.length());
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    public static String join(List<?> strs, String sep) {
        StringBuilder builder = new StringBuilder();
        if (strs == null) {
            return null;
        }
        if (sep == null) {
            sep = "";
        }
        for (int i = 0; i < strs.size(); ++i) {
            if (i > 0) {
                builder.append(sep);
            }
            builder.append(strs.get(i));
        }
        return builder.toString();
    }

    public static Object joinParams(String sep,String... strs) {
        if(strs==null||strs.length==0){
            return " * ";
        }
        StringBuilder builder = new StringBuilder();
        if (strs == null) {
            return null;
        }
        if (sep == null) {
            sep = "";
        }
        for (int i = 0; i < strs.length; ++i) {
            if (i > 0) {
                builder.append(sep);
            }
            builder.append(StringUtil.underscoreName(strs[i]));
        }
        return builder;
    }

    /**
     * eg. "HelloWorld" to "HELLO_WORLD"
     * @param name name to underscore
     * @return underscored name             
     */
    public static String underscoreName(String name) {
        if(name ==null) {
            return null;
        }
        int length =  name.length()*3/2;
        char[] rs = new char[length];
        int i=1;
        char tmp;
        rs[0] = Character.toUpperCase(name.charAt(0));
        for(int j=1;j<name.length();j++){
            tmp = name.charAt(j);
            if(tmp>='A'&&tmp<='Z'){
                rs[i] = '_';
                i++;
                rs[i] = tmp;
                i++;
            }else{
                rs[i] =  Character.toUpperCase(tmp);
                i++;
            }
        }
        return String.valueOf(rs,0,i);
    }


    /**
     * eg. "HELLO_WORLD" to "HelloWorld"
     * @param name name to camel
     * @return cameled name
     */
    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        if (name == null || name.isEmpty()) {
            return "";
        } else if (!name.contains("_")) {
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        String camels[] = name.split("_");
        for (String camel : camels) {
            if (camel.isEmpty()) {
                continue;
            }
            if (result.length() == 0) {
                result.append(camel.toLowerCase());
            } else {
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    public static ByteArrayOutputStream readFullyInputStreamToBytesStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        final int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        do {
            int readSize = inputStream.read(buffer, 0, bufferSize);
            if (readSize < 1) {
                break;
            }
            byteArrayOutputStream.write(buffer, 0, readSize);
        } while (true);
        return byteArrayOutputStream;
    }

    public static String readFullyInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = readFullyInputStreamToBytesStream(inputStream);
            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } finally {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        }
    }
}
