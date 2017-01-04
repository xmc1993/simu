package cn.superid.jpa.orm;

import cn.superid.jpa.annotation.Cacheable;
import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.redis.RedisUtil;
import cn.superid.jpa.redis.BinaryUtil;
import cn.superid.jpa.util.StringUtil;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


public class ModelMeta {
    private final static int fieldsNum = 30;
    private final static int cachedClassNum = 200;
    private Class<?> modelCls;
    private String tableName;
    private String tableSchema;
    private String insertSql;
    private String updateSql;
    private String deleteSql;
    private String findByIdSql;


    private boolean cacheable  =false;
    private byte[] key =null;
    private byte[][] fields;
    private List<ModelColumnMeta> columnMetas;
    private ModelColumnMeta idColumnMeta;
    private ModelColumnMeta partitionColumn;
    /**
     * column info of orm model class, ignore all fields with @javax.sql.Transient
     */
    public static class ModelColumnMeta {
        public boolean isId = false;
        public boolean isPartition = false;
        public String fieldName;
        public String columnName;
        public byte[] binary;
        public Class<?> fieldType;
        public boolean nullable;
        public FieldAccessor fieldAccessor;
    }

    private static List<String> registedKeys = new ArrayList<>();

    /**
     * init column meta and cache it
     * @return
     */
    private List<ModelColumnMeta> getColumnMetas() {
        Field[] fields = modelCls.getDeclaredFields();
        List<ModelColumnMeta> columnMetas = new ArrayList<>(fieldsNum);
        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            FieldAccessor fieldAccessor = new FieldAccessor(modelCls, field.getName());
            if (fieldAccessor.getPropertyAnnotation(Transient.class) != null) {
                continue;
            }
            ModelColumnMeta columnMeta = new ModelColumnMeta();
            columnMeta.fieldAccessor = new FieldAccessor(modelCls, field.getName());

            columnMeta.fieldName = field.getName();
            columnMeta.fieldType = field.getType();
            columnMeta.binary= BinaryUtil.toBytes(columnMeta.fieldName);

            javax.persistence.Column columnAnno = fieldAccessor.getPropertyAnnotation(javax.persistence.Column.class);
            if (columnAnno == null) {
                columnMeta.columnName = StringUtil.underscoreName(field.getName());
                columnMeta.nullable = true;
            } else {
                columnMeta.nullable = columnAnno.nullable();
                if (StringUtil.isEmpty(columnAnno.name())) {
                    columnMeta.columnName = StringUtil.underscoreName(field.getName());
                } else {
                    columnMeta.columnName = columnAnno.name();
                }
            }



            if (fieldAccessor.getPropertyAnnotation(javax.persistence.Id.class) != null) {
                columnMeta.isId = true;
                this.idColumnMeta = columnMeta;
            }else if(fieldAccessor.getPropertyAnnotation(PartitionId.class)!=null){
                columnMeta.isPartition = true;
                this.partitionColumn = columnMeta;
            }

            columnMetas.add(columnMeta);
        }


        this.columnMetas = columnMetas;
        if(this.idColumnMeta!=null){
            initInsertSql();
            initUpdateSql();
            initFindByIdSql();
            initDeleteSql();
        }
        return columnMetas;
    }




    public String getInsertSql() {
        return this.insertSql;
    }

    public void initInsertSql() {//init insert sql
        boolean first = true;
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(this.getTableName());
        sql.append(" (");

        for(ModelColumnMeta columnMeta:columnMetas){
            if(first){
                first =false;
            }else{
                sql.append(",");
            }
            sql.append(columnMeta.columnName);
        }

        sql.append(")");
        sql.append(" VALUES (");
        int size = this.getColumnMetaSet().size();
        for(int i=0;i<size;i++){
            if(i!=0){
                sql.append(",");
            }
            sql.append('?');
        }
        sql.append(")");
        this.insertSql = sql.toString();
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void initUpdateSql() {
        boolean first = true;

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(this.getTableName());
        sql.append(" SET ");

        for(ModelColumnMeta columnMeta:columnMetas){
             if(!columnMeta.isId&&!columnMeta.isPartition){// id and partitionId can't set
                 if(first){
                     first = false;
                 }else{
                     sql.append(",");
                 }
                 sql.append(columnMeta.columnName);
                 sql.append("=? ");
             }
        }

        sql.append(" WHERE ");
        generatePartitionCondition(sql);
        sql.append(this.idColumnMeta.columnName);
        sql.append("=?");

        this.updateSql = sql.toString();
    }

    public String getDeleteSql() {
        return deleteSql;
    }

    public void initDeleteSql() {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(this.getTableName());
        sql.append(" WHERE ");
        generatePartitionCondition(sql);
        sql.append(this.idColumnMeta.columnName);
        sql.append("=?");
        this.deleteSql = sql.toString();
    }


    private void generatePartitionCondition(StringBuilder sql){
        if(this.partitionColumn!=null){
            sql.append(this.partitionColumn.columnName);
            sql.append("=? and ");
        }
    }

    public String getFindByIdSql() {
        return findByIdSql;
    }

    public void initFindByIdSql() {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(this.getTableName());
        sql.append(" WHERE ");
        generatePartitionCondition(sql);
        sql.append(this.idColumnMeta.columnName);
        sql.append("=?");
        this.findByIdSql = sql.toString();
    }


    private static final Map<Class<?>, ModelMeta> modelMetaCache = new ConcurrentHashMap<>(cachedClassNum);

    public static ModelMeta getModelMeta(Class<?> modelCls) {
        ModelMeta modelMeta = modelMetaCache.get(modelCls);
        if(modelMeta==null) {
            synchronized (modelMetaCache) {
                if(modelMetaCache.get(modelCls)==null) {
                    modelMetaCache.put(modelCls, new ModelMeta(modelCls));
                }
            }
            modelMeta = modelMetaCache.get(modelCls);
        }
        return modelMeta;
    }

    private ModelMeta(Class<?> modelCls) {
        this.modelCls = modelCls;

        javax.persistence.Table table = modelCls.getAnnotation(javax.persistence.Table.class);
        tableName = StringUtil.underscoreName(modelCls.getSimpleName());
        tableSchema = "";
        if (table != null) {
            if (!StringUtil.isEmpty(table.name())) {
                tableName = table.name();
            }
            tableSchema = table.schema();
        }
        columnMetas = getColumnMetas();

        Cacheable cacheable = modelCls.getAnnotation(Cacheable.class);
        if(cacheable!=null){
            this.cacheable = true;
            String key =cacheable.key();
            if(StringUtil.isEmpty(key)){
                key = tableName;
            }
            if(registedKeys.contains(key)){//key不能重复
                throw new RuntimeException(tableName+" key is Repeated");
            }
            registedKeys.add(key);
            this.key = BinaryUtil.toBytes(key+':');
        }
    }

    public Class<?> getModelCls() {
        return modelCls;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public List<ModelColumnMeta> getColumnMetaSet() {
        return columnMetas;
    }

    public Iterator<ModelColumnMeta> iterateColumnMetas() {
        return columnMetas.iterator();
    }

    public ModelColumnMeta getIdColumnMeta() {
        return idColumnMeta;
    }



    public Map<String, String> getColumnToPropertyOverrides() {
        Map<String, String> overrides = new HashMap<String, String>();
        for (ModelColumnMeta modelColumnMeta : getColumnMetaSet()) {
            overrides.put(modelColumnMeta.columnName.toLowerCase(), modelColumnMeta.fieldName);
        }
        return overrides;
    }


    public FieldAccessor getIdAccessor() {
        if (idColumnMeta == null) {
            return null;
        }
        return idColumnMeta.fieldAccessor;
    }

    public byte[] getKey() {
        return key;
    }

    public ModelColumnMeta getPatitionColumn(){
        return this.partitionColumn;
    }

    public String getIdName(){
        return this.idColumnMeta.columnName;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    /**
     * 获取redis hmget的filed
     * @return
     */
    private ReentrantLock lockFieldsInit =new ReentrantLock();
    public  byte[][] getCachedFields(){
        if(fields==null){
            lockFieldsInit.lock();
            fields = new byte[columnMetas.size()][];
            int i=0;
            fields[i++] = RedisUtil.getHmFeature();
            for(ModelColumnMeta modelColumnMeta:columnMetas){
                if(!modelColumnMeta.isId){
                    fields[i++] = modelColumnMeta.binary;
                }
            }
            lockFieldsInit.unlock();
        }
        return fields;
    }
}
