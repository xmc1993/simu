package cn.superid.jpa.orm;
import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.jpa.util.*;
import java.util.List;
import java.util.Map;

/**
 * Created by zp on 2016/7/21.
 */
public class Dao<T> {
    protected final static String whereStr = " WHERE (1=1) ";
    protected final static String fromStr = " FROM ";
    protected Class<?> clazz;
    protected static short whereLength = (short)(whereStr.length());
    protected static short fromLength = (short)(fromStr.length());
    protected static ThreadLocal<StringBuilder> where = new ThreadLocal<StringBuilder>(){
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(whereStr);
        }
    };
    protected static ThreadLocal<StringBuilder> from = new ThreadLocal<StringBuilder>(){
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(fromStr);
        }
    };

    protected static ThreadLocal<ParameterBindings> parameterBindings = new ThreadLocal<ParameterBindings>(){
        @Override
        protected ParameterBindings initialValue() {
            return new ParameterBindings();
        }
    };


    public Dao(){}

    public Dao(Class cls){
        this.clazz =cls;
    }

    public static Session getSession() {
        return AbstractSession.currentSession();
    }



    public  T findOne(String sql,Object... params){
        return (T)getSession().findOne(this.clazz,sql,params);
    }



    public List<T> findList(String sql,Object... params){
        return (List<T> )getSession().findList(this.clazz, sql, params);
    }

    public T findOne(String sql,ParameterBindings parameterBindings){
        return (T) getSession().findOne(this.clazz,sql,parameterBindings);
    }



    public   List<T> findList(String sql,ParameterBindings parameterBindings){
        return (List<T>)getSession().findList(this.clazz, sql, parameterBindings);
    }


    public T findById(Object id){

        return  (T) getSession().find(this.clazz,id);
    }






    public T findTinyById(Object id){
        ModelMeta modelMeta = ModelMetaFactory.getEntityMetaOfClass(this.clazz);
        if(modelMeta.isCacheable()){
            throw new JdbcRuntimeException("You should not use this method");
        }
        return (T)getSession().findTiny(this.clazz,id);
    }



    public T findById(Object id,Object partitionId){
        return  (T) getSession().find(this.clazz,id,partitionId);
    }


    public T findTinyById(Object id,Object partitionId){
        return (T)getSession().findTiny(this.clazz,id,partitionId);
    }




    public static Object findById(Class<?> cls,Object id){
        return  getSession().find(cls,id);
    }

    public T selectOne(String... params){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" SELECT ");
        sb.append(StringUtil.joinParams(",",params));
        StringBuilder from = getFrom();
        sb.append(from);
        sb.append(builder);
        sb.append(" limit 1");
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        clear();
        return (T)AbstractSession.currentSession().findOne(this.clazz,sql,sqlParams);
    }



    public Object selectOneByJoin(Class target,String... params){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" SELECT ");
        sb.append(StringUtil.joinParams(",",params));
        StringBuilder from = getFrom();

        sb.append(from);

        sb.append(builder);
        sb.append(" limit 1");
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        clear();
        return AbstractSession.currentSession().findOne(target,sql,sqlParams);
    }

    public int count(){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" SELECT count(id) ");
        StringBuilder from = getFrom();
        sb.append(from);
        sb.append(builder);
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        clear();
        return (int) AbstractSession.currentSession().findOne(Integer.class,sql,sqlParams);
    }

    public int sum(String param){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" SELECT sum("+param+") ");
        StringBuilder from = getFrom();
        sb.append(from);
        sb.append(builder);
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        clear();
        return (int) AbstractSession.currentSession().findOne(Integer.class,sql,sqlParams);
    }

    public List<Integer> sumList(String param){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" SELECT sum("+param+") ");
        StringBuilder from = getFrom();
        sb.append(from);
        sb.append(builder);
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        clear();
        return  AbstractSession.currentSession().findList(Integer.class,sql,sqlParams);
    }





    public List<T> selectByPagination(Pagination pagination,String... params){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" SELECT count(id) ");
        StringBuilder from = getFrom();
        sb.append(from);
        sb.append(builder);
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        int total = (int) AbstractSession.currentSession().findOne(Integer.class,sb.toString(),sqlParams);
        pagination.setTotal(total);


        StringBuilder list = new StringBuilder(" SELECT ");
        list.append(StringUtil.joinParams(",",params));
        list.append(from);
        list.append(builder);
        list.append(" limit ?,? ");
        parameterBindings.get().addIndexBinding(pagination.getOffset());
        parameterBindings.get().addIndexBinding(pagination.getSize());
        Object[] listParams =parameterBindings.get().getIndexParametersArray();
        clear();
        return (List<T>) AbstractSession.currentSession().findList(this.clazz, list.toString(), listParams);

    }



    public List<T> selectList(String... params){
        return (List<T>) this.selectList(this.clazz, params);
    }

    public List<?> selectList(Class<?> clazz,String... params){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" SELECT ");
        sb.append(StringUtil.joinParams(",",params));
        StringBuilder from = getFrom();
        sb.append(from);
        sb.append(builder);
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        clear();
        return (List<Object>) AbstractSession.currentSession().findList(clazz, sql, sqlParams);
    }

    public List<Object> selectListByJoin(Class target,String... params){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" SELECT ");
        sb.append(StringUtil.joinParams(",",params));
        StringBuilder from = getFrom();
        sb.append(from);
        sb.append(builder);
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        clear();
        return  AbstractSession.currentSession().findList(target, sql, sqlParams);

    }

    /**
     * 以下几种set方法用于更新时
     *
     * @param params columnName,value,columnName,value的形式
     * @return 更新的行数
     */
    public  int set(Object... params) {
        if(params==null||params.length==0||params.length%2!=0){
            throw new JdbcRuntimeException("Error update set");
        }

        StringBuilder builder = getWhere();
        ParameterBindings pb = new ParameterBindings();
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(ModelMetaFactory.getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(" SET ");
        int i=0;
        for(Object o:params){
            if(i%2==0){
                if(i!=0){
                    sb.append(",");
                }
                sb.append(StringUtil.underscoreName((String)o));
                sb.append("=?");
            }else{
                pb.addIndexBinding(o);
            }
            i++;
        }
        sb.append(builder);
        String sql = sb.toString();
        ParameterBindings all = pb.addAll(parameterBindings.get());
        clear();
        return getSession().execute(sql,all);
    }

    public  int set(Expr... exprs) {
        if(exprs==null||exprs.length==0){
            throw new JdbcRuntimeException("Error update set");
        }

        StringBuilder builder = getWhere();
        ParameterBindings pb = new ParameterBindings();
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(ModelMetaFactory.getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(" SET ");
        boolean init =true;
        for(Expr expr:exprs){
            if(init){
                init = false;
            }else{
                sb.append(',');
            }
            sb.append(expr.getSql());
            if(expr.getRight()!=null){
                pb.addIndexBinding(expr.getRight());
            }

        }
        sb.append(builder);
        String sql = sb.toString();
        ParameterBindings all = pb.addAll(parameterBindings.get());
        clear();
        return getSession().execute(sql,all);
    }

    public  int set(Map<String,Object> map) {
        StringBuilder builder = getWhere();
        ParameterBindings pb = new ParameterBindings();
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(ModelMetaFactory.getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(" SET ");
        boolean init =true;
        for(String key:map.keySet()){
            if(init){
                init = false;
            }else {
                sb.append(',');
            }
            sb.append(StringUtil.underscoreName(key));
            sb.append("=?");
            pb.addIndexBinding(map.get(key));
        }
        sb.append(builder);
        String sql = sb.toString();
        ParameterBindings all = pb.addAll(parameterBindings.get());
        clear();
        return getSession().execute(sql,all);
    }

    public int setByObject(Object from){
        StringBuilder builder = getWhere();
        ModelMeta meta = ModelMetaFactory.getEntityMetaOfClass(from.getClass());
        ParameterBindings pb = new ParameterBindings();
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(ModelMetaFactory.getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(" SET ");
        boolean init =true;
        for (ModelMeta.ModelColumnMeta modelColumnMeta : meta.getColumnMetaSet()) {
            if(modelColumnMeta.isId){
                continue;
            }
            FieldAccessor fieldAccessor = modelColumnMeta.fieldAccessor;
            Object value = fieldAccessor.getProperty(from);
            if(value==null){
                continue;
            }
            if(init){
                init = false;
            }else {
                sb.append(',');
            }
            sb.append(modelColumnMeta.columnName);
            sb.append("=?");
            pb.addIndexBinding(value);
        }
        sb.append(builder);
        String sql = sb.toString();
        ParameterBindings all = pb.addAll(parameterBindings.get());
        clear();
        return getSession().execute(sql,all);

    }


    public int set(String setSql,ParameterBindings setParams){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(ModelMetaFactory.getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(" SET ");
        sb.append(setSql);
        sb.append(builder);
        String sql = sb.toString();
        ParameterBindings all  ;
        if(setParams==null){
            setParams = new ParameterBindings();
        }
        all= setParams.addAll(parameterBindings.get());

        clear();
        return getSession().execute(sql,all);

    }

    /**
     * remove with conditions
     * @return
     */
    public  int remove() {
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" DELETE FROM ");
        sb.append(ModelMetaFactory.getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(builder);
        String sql = sb.toString();
        Object[] params = parameterBindings.get().getIndexParametersArray();
        clear();
        return getSession().execute(sql,params);
    }

    /**
     * get auto_increment_id ,when DRDS it get Sequence
     * @return
     */
    public long getDRDSAutoId(){
        Session session=getSession();
        ModelMeta modelMeta= ModelMetaFactory.getEntityMetaOfClass(this.clazz);
        StringBuilder stringBuilder=new StringBuilder("select AUTO_SEQ_");
        stringBuilder.append(modelMeta.getTableName());
        stringBuilder.append(".NEXTVAL");
        return  (Long) session.findOne(Long.class,stringBuilder.toString());
    }


    public int execute(String sql,ParameterBindings parameterBindings1){
        return getSession().execute(sql,parameterBindings1);
    }



    /**
        check  meet the conditions
     **/

    public boolean exists(){
        StringBuilder builder = getWhere();
        StringBuilder sb = new StringBuilder(" SELECT 1 ");
        StringBuilder fromBuilder = getFrom();
        sb.append(fromBuilder);
        sb.append(builder);
        sb.append(" limit 1");
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        clear();
        return AbstractSession.currentSession().findOne(Integer.class,sql,sqlParams)!=null;
    }

    private  StringBuilder getFrom(){
        StringBuilder fromBuilder = from.get();
        if(fromBuilder.length() == fromLength){
            fromBuilder  = new StringBuilder(fromStr);
            fromBuilder.append(ModelMetaFactory.getEntityMetaOfClass(this.clazz).getTableName());
        }
        return  fromBuilder;
    }

    private StringBuilder getWhere(){
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should has where conditions");
        }
        return builder;
    }

    private void clear(){
        from.get().delete(fromLength,from.get().length());
        where.get().delete(whereLength,where.get().length());
        parameterBindings.get().clear();
    }



}
