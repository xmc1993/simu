package cn.superid.jpa.core.impl;

import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Transaction;
import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.jpa.jdbcorm.ModelMeta;
import cn.superid.jpa.jdbcorm.sqlmapper.MySQLMapper;
import cn.superid.jpa.jdbcorm.sqlmapper.SqlMapper;
import cn.superid.jpa.query.ParameterBindings;

import cn.superid.jpa.util.FieldAccessor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.exception.CloneFailedException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdbcSession extends AbstractSession {
    private Connection jdbcConnection;
    private JdbcSessionFactory jdbcSessionFactory;
    private AtomicBoolean activeFlag = new AtomicBoolean(false);
    private SqlMapper sqlMapper = new MySQLMapper();
    private transient boolean isInBatch = false;
    private transient PreparedStatement batchStatement;
    private transient boolean closed = false;



    public AtomicBoolean getActiveFlag() {
        return activeFlag;
    }

    public JdbcSession(Connection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    public JdbcSession(JdbcSessionFactory jdbcSessionFactory) {
        this.jdbcSessionFactory = jdbcSessionFactory;
    }

    public synchronized Connection getJdbcConnection() {
        if (jdbcConnection == null) {
            jdbcConnection = jdbcSessionFactory.createJdbcConnection();
        }
        return jdbcConnection;
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            getJdbcConnection().setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    public boolean getAutoCommit() {
        try {
            return getJdbcConnection().getAutoCommit();
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    public void setSqlMapper(SqlMapper sqlMapper) {
        this.sqlMapper = sqlMapper;
    }

    @Override
    public SqlMapper getSqlMapper() {
        return sqlMapper;
    }

    @Override
    public Transaction getTransaction() {
        return new JdbcTransaction(this);
    }

    @Override
    public boolean isOpen() {
        try {
            if(closed) {
                return false;
            }
            if(jdbcConnection==null) {
                return true;
            }
            return !jdbcConnection.isClosed();
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (getTransactionNestedLevel() > 0) {
            return;
        }
        try {
            if(closed) {
                return;
            }
            if(jdbcConnection!=null) {
                jdbcConnection.close();
            }
            closed = true;
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    @Override
    public void shutdown() {
        if (jdbcSessionFactory != null) {
            jdbcSessionFactory.close();
        }
    }



    @Override
    public void save(Object entity) {
        try {
            final ModelMeta modelMeta = getEntityMetaOfClass(entity.getClass());
            int i=getIndexParamBaseOrdinal();
            if (!isInBatch) {
                PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(modelMeta.getInsertSql());
                for (ModelMeta.ModelColumnMeta columnMeta : modelMeta.getColumnMetaSet()) {
                    FieldAccessor fieldAccessor = FieldAccessor.getFieldAccessor(modelMeta.getModelCls(), columnMeta.fieldName);
                    Object value = fieldAccessor.getProperty(entity);
                    preparedStatement.setObject(i,value);
                    i++;
                }
                try {
                    int changedCount = preparedStatement.executeUpdate();
                    if (changedCount < 1) {
                        throw new JdbcRuntimeException("No record affected when save entity");
                    }
                    FieldAccessor idAccessor = modelMeta.getIdAccessor();
                    if (idAccessor != null && idAccessor.getProperty(entity) == null) {
                        ResultSet generatedKeysResultSet = preparedStatement.getGeneratedKeys();
                        try {
                            if (generatedKeysResultSet.next()) {
                                Object generatedId = generatedKeysResultSet.getObject(1);
                                idAccessor.setProperty(entity, generatedId);
                            }
                        } finally {
                            generatedKeysResultSet.close();
                        }
                    }
                } finally {
                    preparedStatement.close();
                }
            } else {
                if (batchStatement == null) {
                    batchStatement = getJdbcConnection().prepareStatement(modelMeta.getInsertSql());
                }
                for (ModelMeta.ModelColumnMeta columnMeta : modelMeta.getColumnMetaSet()) {
                    FieldAccessor fieldAccessor = FieldAccessor.getFieldAccessor(modelMeta.getModelCls(), columnMeta.fieldName);
                    Object value = fieldAccessor.getProperty(entity);
                    batchStatement.setObject(i, value);
                    i++;
                }
                batchStatement.addBatch();
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    //TODO 调用缓存，比较大字段有没有被改动，如果没有改动，则无视大字段
    @Override
    public void update(final Object entity) {
        try {
            final ModelMeta modelMeta = getEntityMetaOfClass(entity.getClass());
            final FieldAccessor idAccessor = modelMeta.getIdAccessor();
            if (!isInBatch) {
                PreparedStatement preparedStatement= getJdbcConnection().prepareStatement(modelMeta.getUpdateSql());
                try {
                    preparedStatement.setObject(getIndexParamBaseOrdinal(),idAccessor.getProperty(entity));
                    preparedStatement.executeUpdate();
                } finally {
                    preparedStatement.close();
                }
            } else {
                if (batchStatement == null) {
                    batchStatement= getJdbcConnection().prepareStatement(modelMeta.getUpdateSql());
                }
                batchStatement.setObject(getIndexParamBaseOrdinal(), idAccessor.getProperty(entity));
                batchStatement.addBatch();
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    @Override
    public void startBatch() {
        this.isInBatch = true;
    }

    @Override
    public void endBatch() {
        this.isInBatch = false;
        if (this.batchStatement != null) {
            try {
                this.batchStatement.close();
            } catch (SQLException e) {
                throw new JdbcRuntimeException(e);
            }
        }
        this.batchStatement = null;
    }

    @Override
    public int[] executeBatch() {
        if (isInBatch && batchStatement != null) {
            try {
                return batchStatement.executeBatch();
            } catch (SQLException e) {
                throw new JdbcRuntimeException(e);
            }
        } else {
            throw new JdbcRuntimeException("Not in batch");
        }
    }

    @Override
    public void updateBatch(List<Object> entities) {
        startBatch();
        try {
            for (Object entity : entities) {
                update(entity);
            }
            executeBatch();
        } finally {
            endBatch();
        }
    }

    @Override
    public void saveBatch(List<Object> entities) {
        startBatch();
        try {
            for (Object entity : entities) {
                save(entity);
            }
            executeBatch();
        } finally {
            endBatch();
        }
    }

    @Override
    public void deleteBatch(List<Object> entities) {
        startBatch();
        try {
            for (Object entity : entities) {
                delete(entity);
            }
            executeBatch();
        } finally {
            endBatch();
        }
    }



    @Override
    public void detach(Object entity) {

    }

    @Override
    public void refresh(Object entity) {
        ModelMeta modelMeta = getEntityMetaOfClass(entity.getClass());
        FieldAccessor idAccessor = modelMeta.getIdAccessor();
        Object latestEntity = find(entity.getClass(), idAccessor.getProperty(entity));
        if (latestEntity != null) {
            try {
                BeanUtils.copyProperties(entity, latestEntity);
            } catch (IllegalAccessException e) {
                throw new CloneFailedException(e);
            } catch (InvocationTargetException e) {
                throw new CloneFailedException(e);
            }
        } else {
            throw new JdbcRuntimeException("Can't find the related record of entity " + entity);
        }
    }

    public static ResultSetHandler<List<Object>> getListResultSetHandler(ModelMeta modelMeta) {
        return new JdbcOrmBeanListHandler(modelMeta.getModelCls(), modelMeta);
    }

    public static ResultSetHandler<Object> getRowBeanResultSetHandler(ModelMeta modelMeta) {
        return new JdbcOrmBeanHandler(modelMeta.getModelCls(), modelMeta);
    }

    /**
     * 根据id查找model
     * @param cls
     * @param id
     * @return
     */
    @Override
    public Object find(Class<?> cls, Object id) {
        try {
            ModelMeta modelMeta = getEntityMetaOfClass(cls);
            ResultSetHandler<List<Object>> handler = getListResultSetHandler(modelMeta);
            PreparedStatement preparedStatement=getJdbcConnection().prepareStatement(modelMeta.getFindByIdSql());
            preparedStatement.setObject(getIndexParamBaseOrdinal(),id);
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                try {
                    List<Object> result = handler.handle(resultSet);
                    if (result.size() > 0) {
                        Object bean = result.get(0);
                        return bean;
                    } else {
                        return null;
                    }
                } finally {
                    resultSet.close();
                }
            } finally {
               preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    @Override
    public void delete(Object entity) {
        try {
            ModelMeta modelMeta = getEntityMetaOfClass(entity.getClass());
            FieldAccessor idAccessor = modelMeta.getIdAccessor();
            if (!isInBatch) {
                PreparedStatement preparedStatement=getJdbcConnection().prepareStatement(modelMeta.getDeleteSql());
                try {
                    preparedStatement.setObject(getIndexParamBaseOrdinal(),idAccessor.getProperty(entity));
                    preparedStatement.executeUpdate();
                } finally {
                    preparedStatement.close();
                }
            } else {
                if (batchStatement == null) {
                    batchStatement = getJdbcConnection().prepareStatement(modelMeta.getDeleteSql());
                }
                batchStatement.setObject(1, idAccessor.getProperty(entity));
                batchStatement.addBatch();
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }


    @Override
    public void flush() {
    }

    @Override
    public List findListByRawQuery(Class<?> cls, String queryString, ParameterBindings parameterBindings) {
        try {
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<Object>> handler = getListResultSetHandler(getEntityMetaOfClass(cls));
            Object[] params = parameterBindings != null ? parameterBindings.getIndexParametersArray() : new Object[0];
            return runner.query(getJdbcConnection(), queryString, handler, params);
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }



    @Override
    public Object findFirstByRawQuery(Class<?> cls, String queryString, ParameterBindings parameterBindings) {
        try {
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<Object>> handler = getListResultSetHandler(getEntityMetaOfClass(cls));
            Object[] params = parameterBindings != null ? parameterBindings.getIndexParametersArray() : new Object[0];
            List<Object> result = runner.query(getJdbcConnection(), queryString, handler, params);
            if (result.size() > 0) {
                return result.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }


}
