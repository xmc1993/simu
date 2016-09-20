package cn.superid.jpa.util;

import org.springframework.security.access.method.P;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by xmc1993 on 16/9/2.
 */
public class ByteUtil {
    public static final Integer LONG_BYTES = 8;
    public static final Integer INT_BYTES = 4;
    public static final Integer DOUBLE_BYTES = 8;
    public static final Integer CHAR_BYRES = 2;
    public static final Integer FLOAT_BYTES = 4;
    public static final Integer SHORT_BYTES = 2;

    public static byte[] getBytes(Object o) {
        if(o == null){
            return new byte[0];
        }
        Class<?> clazz = o.getClass();
        if(clazz == Long.class || clazz == long.class){
            return longToBytes((Long)o);
        }else if(clazz == Integer.class || clazz == int.class){
            return intToBytes((Integer)o);
        }else if(clazz == Float.class || clazz == float.class){
            return floatToBytes((Float) o);
        }else if(clazz == Double.class || clazz == double.class){
            return doubleToBytes((Double) o);
        }else if(clazz == Short.class || clazz == short.class){
            return shortToBytes((Short) o);
        }else if(clazz == Boolean.class || clazz == boolean.class){
            return booleanToBytes((Boolean) o);
        }else if(clazz == Character.class || clazz == char.class){
            return charToBytes((Character) o);
        }else if(clazz == String.class){
            return stringToBytes((String) o);
        }else if(clazz == Date.class){
            return stringToBytes((String) o);
        }else if(clazz== Timestamp.class){
            return timestampToBytes((Timestamp) o);
        }
        else {
            throw new RuntimeException("Error: The parameter" + clazz + "is not basic type!");
        }
    }


    public static Object getValue(byte[] bytes,Class<?> clazz) {
        if(clazz == Long.class || clazz == long.class){
            return  ByteUtil.bytesToLong(bytes);
        }else if(clazz == Integer.class || clazz == int.class){
            return  ByteUtil.bytesToInt(bytes);
        }else if(clazz == Float.class || clazz == float.class){
            return  ByteUtil.bytesToFloat(bytes);
        }else if(clazz == Double.class || clazz == double.class){
            return  ByteUtil.bytesToDouble(bytes);
        }else if(clazz == Short.class || clazz == short.class){
            return ByteUtil.bytesToShort(bytes);
        }else if(clazz == Boolean.class || clazz == boolean.class){
            return  ByteUtil.bytesToBoolean(bytes);
        }else if(clazz == Character.class || clazz == char.class){
            return  ByteUtil.bytesToChar(bytes);
        }else if(clazz == String.class){
            return  ByteUtil.bytesToString(bytes);
        }else if(clazz == Date.class){
            return ByteUtil.bytesToDate(bytes);
        }else if(clazz== Timestamp.class){
            return bytesToTimestamp(bytes);
        }else {
            throw new RuntimeException("Error: The parameter" + clazz + "is not basic type!");
        }
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(LONG_BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(LONG_BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(INT_BYTES);
        buffer.putInt(x);
        return buffer.array();
    }

    public static int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(INT_BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    public static byte[] charToBytes(char x) {
        ByteBuffer buffer = ByteBuffer.allocate(CHAR_BYRES);
        buffer.putChar(x);
        return buffer.array();
    }

    public static char bytesToChar(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(CHAR_BYRES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getChar();
    }

    public static byte[] floatToBytes(float x) {
        ByteBuffer buffer = ByteBuffer.allocate(FLOAT_BYTES);
        buffer.putFloat(x);
        return buffer.array();
    }

    public static float bytesToFloat(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(FLOAT_BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getFloat();
    }

    public static byte[] shortToBytes(short x) {
        ByteBuffer buffer = ByteBuffer.allocate(SHORT_BYTES);
        buffer.putShort(x);
        return buffer.array();
    }

    public static short bytesToShort(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(SHORT_BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getShort();
    }

    public static byte[] booleanToBytes(boolean x) {
        byte[] res = {0x00};
        if(x){
            res[0] = 0x01;
        }
        return res;
    }

    public static Object bytesToBoolean(byte[] bytes) {
        if(bytes[0] == 0x00){
            return false;
        }
        return true;
    }

    public static byte[] doubleToBytes(double x) {
        ByteBuffer buffer = ByteBuffer.allocate(DOUBLE_BYTES);
        buffer.putDouble(x);
        return buffer.array();
    }

    public static byte[] timestampToBytes(Timestamp timestamp){
        return ByteUtil.longToBytes(timestamp.getTime());
    }

    public static Timestamp bytesToTimestamp(byte[] bytes){
        return new Timestamp(bytesToLong(bytes));
    }

    public static double bytesToDouble(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(DOUBLE_BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getDouble();
    }

    public static byte[] stringToBytes(String x)  {
        try {
            return x.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bytesToString(byte[] bytes)  {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] dateToBytes(Date x)  {
        long time = x.getTime();
        return longToBytes(time);
    }

    public static Date bytesToDate(byte[] bytes)  {
        Long o = (Long)bytesToLong(bytes);
        return new Date(o);
    }

}
