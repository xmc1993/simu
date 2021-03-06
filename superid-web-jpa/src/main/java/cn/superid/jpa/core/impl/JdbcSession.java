package cn.superid.jpa.core.impl;

import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Transaction;
import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.jpa.orm.ModelMeta;
import cn.superid.jpa.orm.FieldAccessor;
import cn.superid.jpa.util.NumberUtil;
import cn.superid.jpa.util.Pagination;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    public static Log LOG = LogFactory.getLog(JdbcSession.class);


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
            if (jdbcConnection == null || jdbcConnection.isClosed()) {
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
                jdbcConnection = null;
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    private void close(PreparedStatement preparedStatement){
        try {
            if(preparedStatement!=null){
                LOG.debug(preparedStatement.toString());
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        } finally {
            close();
        }
    }

    @Override
    public void shutdown() {
        try {
            if (jdbcSessionFactory != null) {
                jdbcSessionFactory.close();
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    private int setStatementAllField(ModelMeta modelMeta, PreparedStatement preparedStatement, Object entity, boolean skipId) {
        int i = getIndexParamBaseOrdinal();
        try {
            for (ModelMeta.ModelColumnMeta columnMeta : modelMeta.getColumnMetaSet()) {
                if (skipId && (columnMeta.isId || columnMeta.isPartition)) continue;
                FieldAccessor fieldAccessor = columnMeta.fieldAccessor;
                Object value = fieldAccessor.getProperty(entity);
                if (columnMeta.isPartition && NumberUtil.isUndefined(value)) {
                    throw new JdbcRuntimeException("Partition Column's value  can't be null:" + columnMeta.columnName);
                }
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
            final ModelMeta modelMeta = ModelMeta.getModelMeta(entity.getClass());
            String sql = modelMeta.getInsertSql();
            if (!isInBatch) {
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = getJdbcConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    setStatementAllField(modelMeta, preparedStatement, entity, false);
                    int changedCount = preparedStatement.executeUpdate();
                    if (changedCount < 1) {
                        throw new JdbcRuntimeException("No record affected when save entity");
                    }
                    FieldAccessor idAccessor = modelMeta.getIdAccessor();

                    if (idAccessor != null) {
                        Object value = idAccessor.getProperty(entity);
                        if (NumberUtil.isUndefined(value)) {
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
                    close(preparedStatement);
                }
            } else {
                if (batchStatement == null) {
                    batchStatement = getJdbcConnection().prepareStatement(sql);
                }
                setStatementAllField(modelMeta, batchStatement, entity, false);
                batchStatement.addBatch();
            }

        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }


    @Override
    public boolean update(final Object entity) {
        try {
            final ModelMeta modelMeta = ModelMeta.getModelMeta(entity.getClass());
            boolean isPartition = modelMeta.getPartitionColumn() != null;
            Object partitionId = null;

            if (isPartition) {
                partitionId = modelMeta.getPartitionColumn().fieldAccessor.getProperty(entity);
                if (NumberUtil.isUndefined(partitionId)) {
                    throw new JdbcRuntimeException("you should update with partition id");
                }
            }
            final FieldAccessor idAccessor = modelMeta.getIdAccessor();
            String sql = modelMeta.getUpdateSql();

            if (!isInBatch) {
                PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(sql);
                int i = setStatementAllField(modelMeta, preparedStatement, entity, true);
                Object id = idAccessor.getProperty(entity);
                if (isPartition) {
                    preparedStatement.setObject(i, partitionId);
                    i = i + 1;

                }
                preparedStatement.setObject(i, id);

                try {
                    return preparedStatement.executeUpdate() > 0;
                } finally {
                    close(preparedStatement);
                }
            } else {
                if (batchStatement == null) {
                    batchStatement = getJdbcConnection().prepareStatement(sql);
                }
                int i = setStatementAllField(modelMeta, batchStatement, entity, true);

                if (isPartition) {
                    batchStatement.setObject(i, partitionId);
                    i = i + 1;

                }
                Object id = idAccessor.getProperty(entity);
                batchStatement.setObject(i, id);
                batchStatement.addBatch();
                return true;
            }

        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }


    @Override
    public boolean update(Object entity, List<String> columns) {
        try {

            final ModelMeta modelMeta = ModelMeta.getModelMeta(entity.getClass());
            if (modelMeta.getPartitionColumn() != null) {
                throw new JdbcRuntimeException(" This method don't support partition entity:" + modelMeta.getPartitionColumn().columnName);
            }

            final FieldAccessor idAccessor = modelMeta.getIdAccessor();
            ParameterBindings parameterBindings = new ParameterBindings();
            StringBuilder sb = new StringBuilder(" UPDATE ");
            sb.append(modelMeta.getTableName());
            sb.append(" SET ");
            boolean init = true;
            for (String column : columns) {
                if (init) {
                    init = false;
                } else {
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
            String sql = sb.toString();
            PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(sql);
            parameterBindings.appendToStatement(preparedStatement);
            try {
                int i = preparedStatement.executeUpdate();
                return i > 0;
            } finally {
                close(preparedStatement);
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    @Override
    public void delete(Object entity) {
        try {
            ModelMeta modelMeta = ModelMeta.getModelMeta(entity.getClass());
            FieldAccessor idAccessor = modelMeta.getIdAccessor();
            boolean isSharding = modelMeta.getPartitionColumn() != null;
            Object partitionId = null;

            if (isSharding) {
                partitionId = modelMeta.getPartitionColumn().fieldAccessor.getProperty(entity);
                if (NumberUtil.isUndefined(partitionId)) {
                    throw new JdbcRuntimeException("you should delete with partition column value:" + modelMeta.getPartitionColumn().columnName);
                }
            }

            String sql = modelMeta.getDeleteSql();
            if (!isInBatch) {
                PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(sql);
                try {
                    int i = getIndexParamBaseOrdinal();
                    if (isSharding) {
                        preparedStatement.setObject(i, partitionId);
                        i = i + 1;

                    }
                    Object id = idAccessor.getProperty(entity);
                    if (NumberUtil.isUndefined(id)) {
                        throw new JdbcRuntimeException("id should not be null");
                    }
                    preparedStatement.setObject(i, id);
                    preparedStatement.executeUpdate();
                } finally {
                    close(preparedStatement);
                }
            } else {
                if (batchStatement == null) {
                    batchStatement = getJdbcConnection().prepareStatement(sql);
                }
                int i = getIndexParamBaseOrdinal();
                if (isSharding) {
                    batchStatement.setObject(i, partitionId);
                    i = i + 1;

                }
                Object id = idAccessor.getProperty(entity);
                batchStatement.setObject(i, id);
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
            }finally {
                close();
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
        ModelMeta modelMeta = ModelMeta.getModelMeta(entity.getClass());
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


    /**
     * @param cls         return Type
     * @param id
     * @param partitionId Distributed db partitionId
     * @return
     */

    @Override
    public <T> T find(Class<?> cls, Object id, Object partitionId) {
        try {
            ModelMeta modelMeta = ModelMeta.getModelMeta(cls);
            String sql = modelMeta.getFindByIdSql();
            ResultSetHandler<List<Object>> handler = getListResultSetHandler(modelMeta);
            ModelMeta.ModelColumnMeta partitionColumn = modelMeta.getPartitionColumn();

            PreparedStatement preparedStatement = getJdbcConnection().prepareStatement(sql);
            int i = getIndexParamBaseOrdinal();
            if (partitionColumn != null) {
                if (NumberUtil.isUndefined(partitionId)) {
                    throw new JdbcRuntimeException("you should have partition column:" + partitionColumn.columnName);
                }
                preparedStatement.setObject(i, partitionId);
                i++;
            }
            preparedStatement.setObject(i, id);
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                try {
                    List result = handler.handle(resultSet);
                    if (result.size() > 0) {
                        return (T)result.get(0);
                    } else {
                        return null;
                    }
                } finally {
                    resultSet.close();
                }
            } finally {
                close(preparedStatement);
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
    public <T> T find(Class<?> cls, Object id) {
        return find(cls, id, null);
    }


    @Override
    public void flush() {
    }

    @Override
    public <T> T findListByNativeSql(Class<?> cls, String queryString, Object... params) {
        try {
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<Object>> handler = getListResultSetHandler(ModelMeta.getModelMeta(cls));
            return (T)runner.query(getJdbcConnection(), queryString, handler, params);
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        } finally {
            close();
        }

    }

    /**
     * @param cls
     * @param queryString       should not append (limit ? , ?) string,limit string generate in this method
     * @param parameterBindings
     * @param pagination
     * @return list, the total number is set in pagination
     */
    @Override
    public <T> T findListByNativeSql(Class<?> cls, String queryString, ParameterBindings parameterBindings, Pagination pagination) {
        //验证sql是否带了order排序
        int lastOrderIndex = queryString.lastIndexOf(" order by");
        int lastRightBracketIndex = queryString.lastIndexOf(")");
        if (lastOrderIndex < lastRightBracketIndex) {
            throw new JdbcRuntimeException("Pagination query need order by one column");
        }

        //查询总量
        if (pagination.isNeedTotal()) {
            int fromIndex = queryString.indexOf(" from");
            StringBuilder countSb = new StringBuilder("select count(1)");
            countSb.append(queryString.substring(fromIndex, lastOrderIndex));
            Integer count = (Integer) findOneByNativeSql(Integer.class, countSb.toString(), parameterBindings);
            pagination.setTotal(count);
        } else pagination.setTotal(-1);

        //按分页查询列表
        StringBuilder querySb = new StringBuilder(queryString);
        querySb.append(" limit ? , ?");
        parameterBindings.addIndexBinding(pagination.getOffset());
        parameterBindings.addIndexBinding(pagination.getSize());
        List list = findListByNativeSql(cls, querySb.toString(), parameterBindings);

        if (list.size() < pagination.getSize() && pagination.getPage() == 1 && pagination.getTotal() > list.size()) {
            pagination.setTotal(list.size());
        }
        return (T) list;
    }


    @Override
    public <T> T findOneByNativeSql(Class<?> cls, String queryString, Object... params) {
        try {
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<Object>> handler = getListResultSetHandler(ModelMeta.getModelMeta(cls));
            List<Object> result = runner.query(getJdbcConnection(), queryString, handler, params);
            if (result.size() > 0) {
                return (T)result.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        } finally {
            close();
        }
    }

    @Override
    public <T> T findOneByNativeSql(Class<?> cls, String queryString, ParameterBindings parameterBindings) {
        return findOneByNativeSql(cls, queryString, parameterBindings.getIndexParametersArray() != null ? parameterBindings.getIndexParametersArray() : new Object[0]);
    }

    @Override
    public <T> T findListByNativeSql(Class<?> cls, String queryString, ParameterBindings parameterBindings) {
        return (T) findListByNativeSql(cls, queryString, parameterBindings.getIndexParametersArray() != null ? parameterBindings.getIndexParametersArray() : new Object[0]);
    }


    @Override
    public int execute(String sql) {
       return execute(sql,new Object[0]);
    }

    @Override
    public int execute(String sql, ParameterBindings parameterBindings) {
        return execute(sql,parameterBindings.getIndexParametersArray());
    }


    @Override
    public int execute(String sql, Object[] params) {
        try {
            if(!isInBatch){
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = getJdbcConnection().prepareStatement(sql);
                    if(params!=null&&params.length>0){
                        int i = getIndexParamBaseOrdinal();
                        for (Object p : params) {
                            preparedStatement.setObject(i, p);
                            i++;
                        }
                    }
                    return preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new JdbcRuntimeException(e);
                } finally {
                    close(preparedStatement);
                }
            }else{
                if(batchStatement==null){
                    batchStatement = getJdbcConnection().prepareStatement(sql);
                }
                if(params!=null&&params.length>0){
                    int i = getIndexParamBaseOrdinal();
                    for (Object p : params) {
                        batchStatement.setObject(i, p);
                        i++;
                    }
                    batchStatement.addBatch();
                }
                return 1;

            }
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }
}
