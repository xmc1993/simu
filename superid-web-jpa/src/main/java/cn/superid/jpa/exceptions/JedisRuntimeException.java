package cn.superid.jpa.exceptions;

/**
 * Created by xiaofengxu on 16/10/9.
 */
public class JedisRuntimeException extends RuntimeException
{
    public JedisRuntimeException(String message) {
        super(message);
    }
}
