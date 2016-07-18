package cn.superid.jpa.core;


/**
 *  on 2015/1/18.
 */
public abstract class AbstractSessionFactory implements cn.superid.jpa.core.SessionFactory {


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

    @Override
    public cn.superid.jpa.core.Session getThreadScopeSession() {
        return sessionThreadLocal.get();
    }

    @Override
    public Session currentSession() {
        return getThreadScopeSession();
    }

}
