package cn.superid.jpa.core;

import java.sql.SQLException;

/**
 * Created by zp on 2016/7/18
 */
public abstract class SessionFactory {

    protected final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<Session>() {
        @Override
        public Session initialValue() {
            return createSession();
        }

        @Override
        public Session get() {
            Session session = super.get();
            if (session != null) {
                if (!session.isOpen()) {
                    this.remove();
                    session = initialValue();
                }
            }
            return session;
        }
    };

    public Session getThreadScopeSession() {
        return sessionThreadLocal.get();
    }

    public Session currentSession() {
        return getThreadScopeSession();
    }

    public  abstract  Session createSession();

    public  abstract void close() throws SQLException;
}
