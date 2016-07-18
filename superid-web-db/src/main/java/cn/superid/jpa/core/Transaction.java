package cn.superid.jpa.core;

public interface Transaction {
    public void begin();

    public void commit();

    public void rollback();

    public boolean isActive();
}
