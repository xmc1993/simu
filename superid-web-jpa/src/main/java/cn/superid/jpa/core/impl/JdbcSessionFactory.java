package cn.superid.jpa.core.impl;

import cn.superid.jpa.core.SessionFactory;
import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.util.Function;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
public class JdbcSessionFactory extends SessionFactory {

    private DataSource dataSource;

    public JdbcSessionFactory(final DataSource dataSource) {
        this.dataSource = dataSource;
        AbstractSession.setDefaultSessionFactoryIfEmpty(this);
    }

    @Override
    public Session createSession() {
        JdbcSession session = new JdbcSession(this);
        return session;
    }

    @Override
    public void close() throws SQLException{
        dataSource.getConnection().close();
    }

    public Connection createJdbcConnection() throws SQLException{
        return dataSource.getConnection();
    }

}
