package cn.superid.jpa.util;

import java.nio.ByteBuffer;

/**
 * Created by xmc1993 on 16/9/2.
 */
public class ByteUtil<T> {

    public static byte[] basicType2Bytes(Object o){
        Class<?> clazz = o.getClass();
        if(clazz == Long.class){
            return longToBytes((Long)o);
        }else if(clazz == Integer.class){
            return intToBytes((Integer)o);
        }else if(clazz == Float.class){
            return floatToBytes((Float) o);
        }else if(clazz == Double.class){
            return doubleToBytes((Double) o);
        }else if(clazz == Short.class){
            return shortToBytes((Short) o);
        }else if(clazz == Boolean.class){
            return booleanToBytes((Boolean) o);
        }else if(clazz == Character.class){
            return charToBytes((Character) o);
        }else {
            throw new RuntimeException("Error: The parameter is not basic type!");
        }

    }

    public static  bytes2BasicType(byte[] bytes, Class<?> clazz){

//        if(clazz == Long.class){
//            return
//        }else if(clazz == Integer.class){
//            return intToBytes((Integer)o);
//        }else if(clazz == Float.class){
//            return floatToBytes((Float) o);
//        }else if(clazz == Double.class){
//            return doubleToBytes((Double) o);
//        }else if(clazz == Short.class){
//            return shortToBytes((Short) o);
//        }else if(clazz == Boolean.class){
//            return booleanToBytes((Boolean) o);
//        }else if(clazz == Character.class){
//            return charToBytes((Character) o);
//        }else {
//            throw new RuntimeException("Error: The parameter is not basic type!");
//        }

    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }

    public static int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    public static byte[] charToBytes(char x) {
        ByteBuffer buffer = ByteBuffer.allocate(Character.BYTES);
        buffer.putChar(x);
        return buffer.array();
    }

    public static char bytesToChar(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Character.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getChar();
    }

    public static byte[] floatToBytes(float x) {
        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
        buffer.putFloat(x);
        return buffer.array();
    }

    public static float bytesToFloat(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getFloat();
    }

    public static byte[] shortToBytes(short x) {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.putShort(x);
        return buffer.array();
    }

    public static short bytesToShort(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
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

    public static boolean booleanToShort(byte[] bytes) {
        if(bytes[0] == 0x00){
            return false;
        }
        return true;
    }

    public static byte[] doubleToBytes(double x) {
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES);
        buffer.putDouble(x);
        return buffer.array();
    }

    public static Double bytesToDouble(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getDouble();
    }

}
