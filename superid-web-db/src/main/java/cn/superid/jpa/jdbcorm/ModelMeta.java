package cn.superid.jpa.jdbcorm;

import cn.superid.jpa.Annotation.NotTooSimple;
import cn.superid.jpa.jdbcorm.sqlmapper.SqlMapper;
import cn.superid.jpa.util.FieldAccessor;
import cn.superid.jpa.util.StringUtil;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;


public class ModelMeta {
    private Class<?> modelCls;
    private String tableName;
    private String tableSchema;
    private String insertSql;
    private String updateSql;
    private String deleteSql;
    private String findByIdSql;
    private String findTinyByIdSql;
    private Set<ModelColumnMeta> columnMetas;
    private ModelColumnMeta idColumnMeta;
    private SqlMapper sqlMapper;

    /**
     * column info of orm model class, ignore all fields with @javax.sql.Transient
     */
    public static class ModelColumnMeta {
        public boolean isId = false;
        public String fieldName;
        public String columnName;
        public Class<?> fieldType;
        public boolean nullable;
    }

    private Set<ModelColumnMeta> getColumnMetas() {
        Field[] fields = modelCls.getDeclaredFields();
        Set<ModelColumnMeta> columnMetas = new HashSet<ModelColumnMeta>();
        StringBuilder insertSb=new StringBuilder();
        StringBuilder updateSb = new StringBuilder();
        StringBuilder findTinySb= new StringBuilder();
        boolean init = true;
        boolean initForTiny = true;
        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            FieldAccessor fieldAccessor = new FieldAccessor(modelCls, field.getName());
            if (fieldAccessor.getPropertyAnnotation(Transient.class) != null) {
                continue;
            }
            ModelColumnMeta columnMeta = new ModelColumnMeta();

            columnMeta.fieldName = field.getName();
            columnMeta.fieldType = field.getType();
            if (fieldAccessor.getPropertyAnnotation(javax.persistence.Id.class) != null) {
                columnMeta.isId = true;
                this.idColumnMeta = columnMeta;
            }
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
            if(init){
                init =false;
            }else{
                insertSb.append(",");
                updateSb.append(",");
            }
            insertSb.append(columnMeta.columnName);
            updateSb.append(columnMeta.columnName);
            updateSb.append("=? ");
            if(fieldAccessor.getPropertyAnnotation(NotTooSimple.class)==null){
                if(initForTiny){
                    initForTiny = false;
                }else{
                    findTinySb.append(",");
                }
                findTinySb.append(columnMeta.columnName);
            }
            columnMetas.add(columnMeta);
        }

        initInsertSql(insertSb.toString());
        initUpdateSql(updateSb.toString());
        initDeleteSql();
        return columnMetas;
    }




    public String getInsertSql() {
        return this.insertSql;
    }

    public void initInsertSql(String columns) {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(this.getTableSchema());
        sql.append("(");
        sql.append(columns);
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

    public void initUpdateSql(String updateSql) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(this.getTableSchema());
        sql.append(" SET ");
        sql.append(updateSql);
        sql.append(" WHERE ");
        sql.append(this.idColumnMeta.columnName);
        sql.append("=?");
        this.updateSql = sql.toString();
    }

    public String getDeleteSql() {
        return deleteSql;
    }

    public void initDeleteSql() {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(this.getTableSchema());
        sql.append(" WHERE ");
        sql.append(this.idColumnMeta.columnName);
        sql.append("=?");
        this.deleteSql = sql.toString();
    }

    public String getFindByIdSql() {
        return findByIdSql;
    }

    public void initFindByIdSql() {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(this.getTableSchema());
        sql.append(" WHERE ");
        sql.append(this.idColumnMeta.columnName);
        sql.append("=?");
        this.findByIdSql = sql.toString();
    }


    public String getFindTinyByIdSql() {
        return findTinyByIdSql;
    }

    public void  initFindTinyByIdSql(String findTinyByIdSql) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(findTinyByIdSql);
        sql.append(" FROM");
        sql.append(" WHERE ");
        sql.append(this.idColumnMeta.columnName);
        sql.append("=?");
        this.findTinyByIdSql = sql.toString();
    }

    private static final Map<Class<?>, ModelMeta> modelMetaCache = new HashMap<Class<?>, ModelMeta>();

    public static ModelMeta getModelMeta(Class<?> modelCls, SqlMapper sqlMapper) {
        ModelMeta modelMeta = modelMetaCache.get(modelCls);
        if(modelMeta==null) {
            synchronized (modelMetaCache) {
                if(modelMetaCache.get(modelCls)==null) {
                    modelMetaCache.put(modelCls, new ModelMeta(modelCls, sqlMapper));
                }
            }
            modelMeta = modelMetaCache.get(modelCls);
        }
        return modelMeta;
    }

    private ModelMeta(Class<?> modelCls, SqlMapper sqlMapper) {
        this.sqlMapper = sqlMapper;
        // get meta info of orm model
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

    public Set<ModelColumnMeta> getColumnMetaSet() {
        return columnMetas;
    }

    public Iterator<ModelColumnMeta> iterateColumnMetas() {
        return columnMetas.iterator();
    }

    public ModelColumnMeta getIdColumnMeta() {
        return idColumnMeta;
    }

    public ModelColumnMeta getColumnMetaByFieldName(String fieldName) {
        for (ModelColumnMeta modelColumnMeta : getColumnMetaSet()) {
            if (modelColumnMeta.fieldName.equals(fieldName)) {
                return modelColumnMeta;
            }
        }
        return null;
    }

    public ModelColumnMeta getColumnMetaBySqlColumnName(String columnName) {
        for (ModelColumnMeta modelColumnMeta : getColumnMetaSet()) {
            if (modelColumnMeta.columnName.equalsIgnoreCase(columnName)) {
                return modelColumnMeta;
            }
        }
        return null;
    }

    public Map<String, String> getColumnToPropertyOverrides() {
        Map<String, String> overrides = new HashMap<String, String>();
        for (ModelColumnMeta modelColumnMeta : getColumnMetaSet()) {
            overrides.put(modelColumnMeta.columnName.toLowerCase(), modelColumnMeta.fieldName);
        }
        return overrides;
    }

    public SqlMapper getSqlMapper() {
        return sqlMapper;
    }

    public FieldAccessor getIdAccessor() {
        if (idColumnMeta == null) {
            return null;
        }
        return FieldAccessor.getFieldAccessor(modelCls, idColumnMeta.fieldName);
    }
}
