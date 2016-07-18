package cn.superid.jpa.core;


public interface SessionFactory {
    cn.superid.jpa.core.Session getThreadScopeSession();

    cn.superid.jpa.core.Session currentSession();

    cn.superid.jpa.core.Session createSession();

    void close();
}
