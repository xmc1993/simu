package cn.superid.jpa.exceptions;

/**
 *  on 15/1/26.
 */
public class JdbcRuntimeException extends RuntimeException {

    public JdbcRuntimeException(String message) {
        super(message);
    }


    public JdbcRuntimeException(Throwable cause) {
        super(cause);
    }

}
