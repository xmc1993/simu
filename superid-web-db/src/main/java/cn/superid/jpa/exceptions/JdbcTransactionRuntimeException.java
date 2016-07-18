package cn.superid.jpa.exceptions;

import cn.superid.jpa.exceptions.*;

/**
 *  on 15/1/26.
 */
public class JdbcTransactionRuntimeException extends cn.superid.jpa.exceptions.JdbcRuntimeException {
    public JdbcTransactionRuntimeException() {
    }

    public JdbcTransactionRuntimeException(String message) {
        super(message);
    }

    public JdbcTransactionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcTransactionRuntimeException(Throwable cause) {
        super(cause);
    }

    public JdbcTransactionRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
