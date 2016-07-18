package cn.superid.jpa.core.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.AbstractSessionFactory;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.jpa.jdbcorm.sqlmapper.MySQLMapper;
import cn.superid.jpa.jdbcorm.sqlmapper.SqlMapper;
import cn.superid.jpa.util.StringUtil;
import cn.superid.jpa.util.cache.CacheManagerBuilder;
import cn.superid.jpa.util.cache.ICache;
import cn.superid.jpa.util.cache.ICacheManager;
import cn.superid.jpa.util.cache.MemoryCacheManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class JdbcSessionFactory extends AbstractSessionFactory {

    private static final String CACHE_NAME = "jpa_utils_cache";

    private transient LoadingCache<Class<?>, ICache<Object, Object>> beanCacheBuilder = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<Class<?>, ICache<Object, Object>>() {
                public ICache<Object, Object> load(Class<?> key) {
                    return getCacheManager().getCache(CACHE_NAME);
                }
            });

    public interface JdbcConnectionSource {
        Connection get();
    }

    private JdbcConnectionSource jdbcConnectionSource;
    private DataSource dataSource;
    private SqlMapper sqlMapper;

    private String cacheManagerClassName;

    public String getCacheManagerClassName() {
        return cacheManagerClassName;
    }

    public void setCacheManagerClassName(String cacheManagerClassName) {
        this.cacheManagerClassName = cacheManagerClassName;
    }

    private ICacheManager cacheManager;

    public synchronized void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public synchronized ICacheManager getCacheManager() {
        if(cacheManager!=null) {
            return cacheManager;
        }
        if(StringUtil.isEmpty(cacheManagerClassName)) {
            cacheManager = new MemoryCacheManager();
        } else {
            try {
                cacheManager = CacheManagerBuilder.createCacheManagerBuilder(cacheManagerClassName);
                if(cacheManager == null) {
                    cacheManager = new MemoryCacheManager();
                }
            } catch (Exception e) {
                cacheManager = new MemoryCacheManager();
            }
        }
        return cacheManager;
    }

    public JdbcSessionFactory(JdbcConnectionSource jdbcConnectionSource, SqlMapper sqlMapper) {
        this.jdbcConnectionSource = jdbcConnectionSource;
        this.sqlMapper = sqlMapper;
        AbstractSession.setDefaultSessionFactoryIfEmpty(this);
    }

    public JdbcSessionFactory(JdbcConnectionSource jdbcConnectionSource) {
        this(jdbcConnectionSource, new MySQLMapper());
    }

    public JdbcSessionFactory(final DataSource dataSource, SqlMapper sqlMapper) {
        this.dataSource = dataSource;
        this.jdbcConnectionSource = new JdbcConnectionSource() {
            @Override
            public Connection get() {
                try {
                    return dataSource.getConnection();
                } catch (SQLException e) {
                    throw new JdbcRuntimeException(e);
                }
            }
        };
        this.sqlMapper = sqlMapper;
        AbstractSession.setDefaultSessionFactoryIfEmpty(this);
    }

    public JdbcSessionFactory(final DataSource dataSource) {
        this(dataSource, new MySQLMapper());
    }

    public SqlMapper getSqlMapper() {
        return sqlMapper;
    }

    public void setSqlMapper(SqlMapper sqlMapper) {
        this.sqlMapper = sqlMapper;
    }

    @Override
    public Session createSession() {
        cn.superid.jpa.core.impl.JdbcSession session = new cn.superid.jpa.core.impl.JdbcSession(this);
        session.setSqlMapper(sqlMapper);
        return session;
    }

    @Override
    public void close() {

    }

    public Connection createJdbcConnection() {
        return jdbcConnectionSource.get();
    }


    private String makeCacheKey(Class<?> beanCls, Object key) {
        if(beanCls == null || key == null) {
            return null;
        } else {
            return beanCls.getName() + "@@" + key;
        }
    }

}
