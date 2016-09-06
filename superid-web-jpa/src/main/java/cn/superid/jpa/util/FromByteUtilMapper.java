package cn.superid.jpa.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by xmc1993 on 16/9/5.
 */
public class FromByteUtilMapper<T> {

    private Class<?> clazz;

    private FromByteUtilMapper(){}

    public FromByteUtilMapper(Class<?> clazz){
        this.clazz = clazz;
    }

    public T bytes2BasicType(byte[] bytes) throws UnsupportedEncodingException {
        if(clazz == Long.class || clazz == long.class){
            return (T) ByteUtil.bytesToLong(bytes);
        }else if(clazz == Integer.class || clazz == int.class){
            return (T) ByteUtil.bytesToInt(bytes);
        }else if(clazz == Float.class || clazz == float.class){
            return (T) ByteUtil.bytesToFloat(bytes);
        }else if(clazz == Double.class || clazz == double.class){
            return (T) ByteUtil.bytesToDouble(bytes);
        }else if(clazz == Short.class || clazz == short.class){
            return (T) ByteUtil.bytesToShort(bytes);
        }else if(clazz == Boolean.class || clazz == boolean.class){
            return (T) ByteUtil.bytesToBoolean(bytes);
        }else if(clazz == Character.class || clazz == char.class){
            return (T) ByteUtil.bytesToChar(bytes);
        }else if(clazz == String.class){
            return (T) ByteUtil.bytesToString(bytes);
        }else if(clazz == Date.class){
            return (T) ByteUtil.bytesToDate(bytes);
        }else {
            throw new RuntimeException("Error: The parameter" + clazz + "is not basic type!");
        }
    }
}
