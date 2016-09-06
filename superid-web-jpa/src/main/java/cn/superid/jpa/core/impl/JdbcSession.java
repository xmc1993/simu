package cn.superid.jpa.core.impl;

import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Transaction;
import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.jpa.orm.ModelMeta;
import cn.superid.jpa.orm.FieldAccessor;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class JdbcSession extends AbstractSession {
    private Connection jdbcConnection;
    private JdbcSessionFactory jdbcSessionFactory;
    private AtomicBoolean activeFlag = new AtomicBoolean(false);
    private transient boolean isInBatch = false;
    private transient PreparedStatement batchStatement;


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
        try {
            if (jdbcConnection == null) {
                jdbcConnection = jdbcSessionFactory.createJdbcConnection();
            }
        } catch (Exception e) {
            throw new JdbcRuntimeException(e);
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

    @Override
    public Transaction getTransaction() {
        return new JdbcTransaction(this);
    }

    @Override
    public boolean isOpen() {
        try {

            if (jdbcConnection == null) {
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
            if (jdbcConnection != null) {
                jdbcConnection.close();
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    @Override
    public void shutdown(){
        try {
            if (jdbcSessionFactory != null) {
                jdbcSessionFactory.close();
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    private int setStatement(ModelMeta modelMeta,PreparedStatement preparedStatement,Object entity,boolean skipId){
        int i = getIndexParamBaseOrdinal();
        try {
            for (ModelMeta.ModelColumnMeta columnMeta : modelMeta.getColumnMetaSet()) {
                if(skipId&&columnMeta.isId) continue;
                FieldAccessor fieldAccessor = FieldAccessor.getFieldAccessor(modelMeta.getModelCls(), columnMeta.fieldName);
                Object value = fieldAccessor.getProperty(entity);
                preparedStatement.setObject(i, value);
                i++;
            }
            return i;
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }

    }

    //TODO 代码简化
    @Override
    public void save(Object entity) {
        try {
            final ModelMeta modelMeta = getEntityMetaOfClass(entity.getClass());
            String sql = modelMeta.getInsertSql();
            if (!isInBatch) {
                PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                setStatement(modelMeta,preparedStatement,entity,false);
                try {

                    int changedCount = preparedStatement.executeUpdate();
                    if (changedCount < 1) {
                        throw new JdbcRuntimeException("No record affected when save entity");
                    }
                    FieldAccessor idAccessor = modelMeta.getIdAccessor();

                    if (idAccessor != null) {
                        Object value = idAccessor.getProperty(entity);
                        if(value==null||value.equals(0)){
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
                    }

                } finally {
                    preparedStatement.close();
                    close();
                }
            } else {
                if (batchStatement == null) {
                    batchStatement = getJdbcConnection().prepareStatement(sql);
                }
                setStatement(modelMeta,batchStatement,entity,false);
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
            String sql = modelMeta.getUpdateSql();

            if (!isInBatch) {
                PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(sql);
                int i= setStatement(modelMeta,preparedStatement,entity,true);
                Object id = idAccessor.getProperty(entity);
                preparedStatement.setObject(i, id);
                try {
                    preparedStatement.executeUpdate();
                } finally {
                    preparedStatement.close();
                    close();
                }
            } else {
                if (batchStatement == null) {
                    batchStatement = getJdbcConnection().prepareStatement(sql);
                }
                int i=setStatement(modelMeta,batchStatement,entity,true);

                Object id = idAccessor.getProperty(entity);
                batchStatement.setObject(i, id);
                batchStatement.addBatch();
            }

        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }


    @Override
    public void update(Object entity, List<String> columns) {
        try {
            final ModelMeta modelMeta = getEntityMetaOfClass(entity.getClass());
            final FieldAccessor idAccessor = modelMeta.getIdAccessor();
            ParameterBindings parameterBindings=new ParameterBindings();
            StringBuilder sb =new StringBuilder(" UPDATE ");
            sb.append(modelMeta.getTableName());
            sb.append(" SET ");
            boolean init = true;
            for(String column:columns){
                if(init){
                    init = false;
                }else{
                    sb.append(",");
                }
                FieldAccessor fieldAccessor = FieldAccessor.getFieldAccessor(modelMeta.getModelCls(), column);
                Object value = fieldAccessor.getProperty(entity);
                sb.append(StringUtil.underscoreName(column));
                sb.append("=? ");
                parameterBindings.addIndexBinding(value);
            }
            sb.append(" WHERE ");
            sb.append(modelMeta.getIdName());
            sb.append("=?");
            parameterBindings.addIndexBinding(idAccessor.getProperty(entity));
            String sql =sb.toString();
            PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(sql);
            parameterBindings.appendToStatement(preparedStatement);
            try {
                preparedStatement.executeUpdate();
            } finally {
                preparedStatement.close();
                close();
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
            String sql = modelMeta.getDeleteSql();
            if (!isInBatch) {
                PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(sql);
                try {
                    Object id = idAccessor.getProperty(entity);
                    preparedStatement.setObject(getIndexParamBaseOrdinal(), id);
                    preparedStatement.executeUpdate();
                } finally {
                    preparedStatement.close();
                    close();
                }
            } else {
                if (batchStatement == null) {
                    batchStatement = getJdbcConnection().prepareStatement(sql);
                }
                Object id = idAccessor.getProperty(entity);
                batchStatement.setObject(getIndexParamBaseOrdinal(), id);
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
                throw new JdbcRuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new JdbcRuntimeException(e);
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


    public Object find(Class<?> cls, Object id, boolean tiny) {
        try {
            String sql;
            ModelMeta modelMeta = getEntityMetaOfClass(cls);
            ResultSetHandler<List<Object>> handler = getListResultSetHandler(modelMeta);
            if (tiny) {
                sql = modelMeta.getFindTinyByIdSql();
            } else {
                sql = modelMeta.getFindByIdSql();
            }
            PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(sql);
            preparedStatement.setObject(getIndexParamBaseOrdinal(), id);
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                try {
                    List result = handler.handle(resultSet);
                    if (result.size() > 0) {
                        Object bean = result.get(0);
                        return result.get(0);
                    } else {
                        return null;
                    }
                } finally {
                    resultSet.close();
                }
            } finally {
                preparedStatement.close();
                close();
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    /**
     * 根据id查找model
     *
     * @param cls
     * @param id
     * @return
     */
    @Override
    public Object find(Class<?> cls, Object id) {
        return find(cls, id, false);
    }



    @Override
    public void flush() {
    }

    @Override
    public List findList(Class<?> cls, String queryString, Object... params) {
        try {
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<Object>> handler = getListResultSetHandler(getEntityMetaOfClass(cls));
            return runner.query(getJdbcConnection(), queryString, handler, params);
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }finally {
            close();
        }

    }


    @Override
    public Object findOne(Class<?> cls, String queryString, Object... params) {
        try {
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<Object>> handler = getListResultSetHandler(getEntityMetaOfClass(cls));
            List<Object> result = runner.query(getJdbcConnection(), queryString, handler, params);
            if (result.size() > 0) {
                return result.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }finally {
            close();
        }
    }

    @Override
    public Object findOne(Class<?> cls, String queryString, ParameterBindings parameterBindings) {
        return findOne(cls, queryString, parameterBindings.getIndexParametersArray() != null ? parameterBindings.getIndexParametersArray() : new Object[0]);
    }

    @Override
    public List findList(Class<?> cls, String queryString, ParameterBindings parameterBindings) {
        return findList(cls, queryString, parameterBindings.getIndexParametersArray() != null ? parameterBindings.getIndexParametersArray() : new Object[0]);
    }

    @Override
    public Object findTiny(Class<?> cls, Object id) {
        return find(cls, id, true);
    }

    @Override
    public int execute(String sql) {
        PreparedStatement preparedStatement =null;
        try {
            preparedStatement = getJdbcConnection().prepareStatement(sql);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }finally {
            if(preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw  new JdbcRuntimeException(e);
                }
            }
            close();
        }

    }

    @Override
    public int execute(String sql, ParameterBindings parameterBindings) {
        PreparedStatement preparedStatement =null;
        try {
            preparedStatement = getJdbcConnection().prepareStatement(sql);
            parameterBindings.appendToStatement(preparedStatement);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }finally {
            if(preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw  new JdbcRuntimeException(e);
                }
            }
            close();
        }
    }

}
