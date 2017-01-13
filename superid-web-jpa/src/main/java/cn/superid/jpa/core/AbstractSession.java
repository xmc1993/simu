package cn.superid.jpa.core;

import cn.superid.jpa.orm.FieldAccessor;
import cn.superid.jpa.orm.ModelMeta;
import cn.superid.jpa.redis.RedisUtil;
import cn.superid.jpa.redis.BinaryUtil;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public abstract class AbstractSession implements Session {

    protected final Queue<Object> txStack = new ConcurrentLinkedQueue<Object>();


    @Override
    public int getIndexParamBaseOrdinal() {
        return 1;
    }

    @Override
    public void begin() {
        txStack.add(1);
        if (getTransactionNestedLevel() > 1) {
            return;
        }
        getTransaction().begin();
    }

    @Override
    public boolean isRunning() {
        return txStack.size() > 0;
    }

    @Override
    public void commit() {
        if (!isOpen()) {
            begin();
        }
        txStack.poll();
        if (getTransactionNestedLevel() > 0) {
            return;
        }
        getTransaction().commit();
    }




    @Override
    public int[] executeBatch() {
        return new int[0];
    }

    @Override
    public void updateBatch(List<Object> entities) {
        for (Object entity : entities) {
            update(entity);
        }
    }

    @Override
    public void saveBatch(List<Object> entities) {
        for (Object entity : entities) {
            save(entity);
        }
    }

    @Override
    public void deleteBatch(List<Object> entities) {
        for (Object entity : entities) {
            delete(entity);
        }
    }


    @Override
    public void rollback() {
        if (!isTransactionActive()) {
            return;
        }
        txStack.poll();
        if (getTransactionNestedLevel() > 0) {
            return;
        }
        getTransaction().rollback();
    }

    @Override
    public boolean isClosed() {
        return !isOpen();
    }

    @Override
    public void closeFully() {
        txStack.clear();
        close();
    }

    @Override
    public int getTransactionNestedLevel() {
        return txStack.size();
    }

    @Override
    public boolean isTransactionActive() {
        return getTransaction().isActive();
    }




    private static transient SessionFactory defaultSessionFactory = null;

    public static void setDefaultSessionFactory(SessionFactory sessionFactory) {
        defaultSessionFactory = sessionFactory;
    }

    public static void setDefaultSessionFactoryIfEmpty(SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            return;
        }
        if (defaultSessionFactory == null) {
            synchronized (AbstractSession.class) {
                if (defaultSessionFactory == null) {
                    defaultSessionFactory = sessionFactory;
                }
            }
        }
    }

    /**
     * check whether session binded to current thread first
     * and set result to binded session of current thread
     *
     * @return current session of current thread
     */
    public static Session currentSession() {

        return defaultSessionFactory.currentSession();
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS
            = new ImmutableMap.Builder<Class<?>, Class<?>>()
            .put(boolean.class, Boolean.class)
            .put(byte.class, Byte.class)
            .put(char.class, Character.class)
            .put(double.class, Double.class)
            .put(float.class, Float.class)
            .put(int.class, Integer.class)
            .put(long.class, Long.class)
            .put(short.class, Short.class)
            .put(void.class, Void.class)
            .build();

    private boolean canSetProperties(ModelMeta.ModelColumnMeta fromColumnMeta,ModelMeta.ModelColumnMeta toColumnMeta){
        if(!toColumnMeta.fieldName.equals(fromColumnMeta.fieldName)){
            return false;
        }
        Class<?> from = fromColumnMeta.fieldType;
        Class<?> to = toColumnMeta.fieldType;
        if(from.equals(to)){
            return true;
        }
        if(from.isPrimitive()){
           return  PRIMITIVES_TO_WRAPPERS.get(from).equals(to);
        }
        if(to.isPrimitive()){
            return PRIMITIVES_TO_WRAPPERS.get(to).equals(from);
        }
        return false;
    }




    @Override
    public void copyProperties(Object from, Object to, boolean skipNull) {

        ModelMeta fromMeta = ModelMeta.getModelMeta(from.getClass());
        ModelMeta toMeta = ModelMeta.getModelMeta(to.getClass());
        for (ModelMeta.ModelColumnMeta fromColumnMeta : fromMeta.getColumnMetaSet()) {
            for (ModelMeta.ModelColumnMeta toColumnMeta : toMeta.getColumnMetaSet()) {
                if (fromColumnMeta.isId && skipNull) {
                    continue;
                }

                if (canSetProperties(fromColumnMeta,toColumnMeta)) {
                    FieldAccessor fromFa = fromColumnMeta.fieldAccessor;
                    Object value = fromFa.getProperty(from);
                    if (skipNull && value == null) {
                        continue;
                    }
                    FieldAccessor toFa = toColumnMeta.fieldAccessor;
                    toFa.setProperty(to, fromFa.getProperty(from));
                }
            }

        }
    }

    @Override
    public HashMap<String, Object> generateHashMapFromEntity(Object entity,boolean skipNull) {
        ModelMeta meta = ModelMeta.getModelMeta(entity.getClass());
        //给定HashMap初始大小 防止过度分配空间浪费
        HashMap<String, Object> hashMap = new HashMap<>(meta.getColumnMetaSet().size());
        for (ModelMeta.ModelColumnMeta modelColumnMeta : meta.getColumnMetaSet()) {
            if(modelColumnMeta.isId&&skipNull){
                continue;
            }
            FieldAccessor fieldAccessor = modelColumnMeta.fieldAccessor;
            Object value = fieldAccessor.getProperty(entity);
            if(value==null&&skipNull){
                continue;
            }

            hashMap.put(modelColumnMeta.fieldName, fieldAccessor.getProperty(entity));
        }
        return hashMap;
    }

    @Override
    public HashMap<byte[], byte[]> generateHashByteMap(Object entity) {
        ModelMeta meta = ModelMeta.getModelMeta(entity.getClass());
        //给定HashMap初始大小 防止过度分配空间浪费
        HashMap<byte[], byte[]> hashMap = new HashMap<>(meta.getColumnMetaSet().size());
        hashMap.put(RedisUtil.getHmFeature(),RedisUtil.getHmFeature());
        for (ModelMeta.ModelColumnMeta modelColumnMeta : meta.getColumnMetaSet()) {
            if(modelColumnMeta.isId){
                continue;
            }
            FieldAccessor fieldAccessor = modelColumnMeta.fieldAccessor;
            hashMap.put(modelColumnMeta.binary, BinaryUtil.getBytes(fieldAccessor.getProperty(entity)));

        }
        return hashMap;
    }

    @Override
    public <T> T generateHashMapFromEntity(HashMap<String, Object> hashMap, Object entity) {
        ModelMeta meta = ModelMeta.getModelMeta(entity.getClass());
        for (ModelMeta.ModelColumnMeta modelColumnMeta : meta.getColumnMetaSet()) {
            FieldAccessor fieldAccessor = modelColumnMeta.fieldAccessor;
            fieldAccessor.setProperty(entity, hashMap.get(modelColumnMeta.fieldName));
        }
        return (T)entity;
    }

    @Override
    public <T> T generateEntityFromHashMap(HashMap<byte[], byte[]> hashMap, Object entity) {
        ModelMeta meta = ModelMeta.getModelMeta(entity.getClass());
        for (ModelMeta.ModelColumnMeta modelColumnMeta : meta.getColumnMetaSet()) {
            FieldAccessor fieldAccessor = modelColumnMeta.fieldAccessor;
            fieldAccessor.setProperty(entity,BinaryUtil.getValue(hashMap.get(modelColumnMeta.binary),fieldAccessor.getPropertyType()));

        }
        return (T) entity;
    }

    @Override
    public byte[][] generateZipMap(Object entity) {
        ModelMeta meta = ModelMeta.getModelMeta(entity.getClass());
        byte[][] result = new byte[(meta.getColumnMetaSet().size()-1)*2+1][];//因为id不需要存入zipmap
        byte[] key=  meta.getKey();
        int i= 1;

        for (ModelMeta.ModelColumnMeta modelColumnMeta : meta.getColumnMetaSet()) {
            if(modelColumnMeta.isId){
                Object id= BinaryUtil.getBytes(meta.getIdAccessor().getProperty(entity));
                byte[] idByte = BinaryUtil.getBytes(id);
                result[0]= new byte[idByte.length+key.length];
                for(int j=0;j<result[0].length;j++){
                    if(j<key.length){
                        result[0][j]=key[j];
                    }else {
                        result[0][j] = idByte[j-key.length];
                    }
                }
                continue;
            }
            result[i++] = modelColumnMeta.binary;


            FieldAccessor fieldAccessor = modelColumnMeta.fieldAccessor;
            Object value = fieldAccessor.getProperty(entity);
            result[i++]= BinaryUtil.getBytes(value);
        }
        return result;
    }
}
