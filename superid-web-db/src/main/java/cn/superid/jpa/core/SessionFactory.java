package cn.superid.jpa.core;

import java.sql.SQLException;

/**
 * Created by zp on 2016/7/18
 */
public abstract class SessionFactory {

    protected final ThreadLocal<cn.superid.jpa.core.Session> sessionThreadLocal = new ThreadLocal<cn.superid.jpa.core.Session>() {
        @Override
        public cn.superid.jpa.core.Session initialValue() {
            return createSession();
        }

        @Override
        public cn.superid.jpa.core.Session get() {
            cn.superid.jpa.core.Session session = super.get();
            if (session != null) {
                if (!session.isOpen()) {
                    this.remove();
                    session = initialValue();
                }
            }
            return session;
        }
    };

    public cn.superid.jpa.core.Session getThreadScopeSession() {
        return sessionThreadLocal.get();
    }

    public Session currentSession() {
        return getThreadScopeSession();
    }

    public  abstract  cn.superid.jpa.core.Session createSession();

    public  abstract void close() throws SQLException;
}
