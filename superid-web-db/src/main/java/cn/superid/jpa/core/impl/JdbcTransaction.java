package cn.superid.jpa.core.impl;

import cn.superid.jpa.core.Transaction;
import cn.superid.jpa.core.impl.*;
import cn.superid.jpa.exceptions.JdbcTransactionRuntimeException;

/**
 *  on 15/1/26.
 */
public class JdbcTransaction implements Transaction {
    private final cn.superid.jpa.core.impl.JdbcSession jdbcSession;


    public JdbcTransaction(cn.superid.jpa.core.impl.JdbcSession jdbcSession) {
        this.jdbcSession = jdbcSession;
    }

    @Override
    public void begin() {
        jdbcSession.setAutoCommit(false);
        jdbcSession.getActiveFlag().set(true);
    }

    @Override
    public void commit() {
        try {
            jdbcSession.getActiveFlag().set(true);
            jdbcSession.getJdbcConnection().commit();
            jdbcSession.getActiveFlag().set(false);
            jdbcSession.setAutoCommit(true);
        } catch (Exception e) {
            throw new JdbcTransactionRuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        if (jdbcSession.getActiveFlag().get()) {
            return;
        }
        try {
            jdbcSession.getActiveFlag().set(false);
            jdbcSession.getJdbcConnection().rollback();
        } catch (Exception e) {
            throw new JdbcTransactionRuntimeException(e);
        }
    }

    @Override
    public boolean isActive() {
        return jdbcSession.getActiveFlag().get();
    }
}
