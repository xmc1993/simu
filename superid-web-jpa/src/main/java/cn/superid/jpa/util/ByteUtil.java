package cn.superid.jpa.util;

import org.springframework.security.access.method.P;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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

    public static byte[] basicType2Bytes(Object o) throws UnsupportedEncodingException {
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
        }else {
            throw new RuntimeException("Error: The parameter" + clazz + "is not basic type!");
        }
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(LONG_BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static Object bytesToLong(byte[] bytes) {
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

    public static Object bytesToInt(byte[] bytes) {
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

    public static Object bytesToChar(byte[] bytes) {
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

    public static Object bytesToFloat(byte[] bytes) {
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

    public static Object bytesToShort(byte[] bytes) {
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

    public static Object bytesToDouble(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(DOUBLE_BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getDouble();
    }

    public static byte[] stringToBytes(String x) throws UnsupportedEncodingException {
        return x.getBytes("UTF-8");
    }

    public static String bytesToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

    public static byte[] dateToBytes(Date x) throws UnsupportedEncodingException {
        long time = x.getTime();
        return longToBytes(time);
    }

    public static Date bytesToDate(byte[] bytes) throws UnsupportedEncodingException {
        Long o = (Long)bytesToLong(bytes);
        return new Date(o);
    }

}
