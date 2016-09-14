package cn.superid.jpa.orm;
import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.jpa.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zp on 2016/7/21.
 */
public class Dao<T> {
    private final static String whereStr = " WHERE (1=1) ";
    private Class<?> clazz;
    private static short whereLength = (short)(whereStr.length());
    private static ThreadLocal<StringBuilder> where = new ThreadLocal<StringBuilder>(){
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(whereStr);
        }
    };
    private static ThreadLocal<ParameterBindings> parameterBindings = new ThreadLocal<ParameterBindings>(){
        @Override
        protected ParameterBindings initialValue() {
            return new ParameterBindings();
        }
    };


    public Dao(){};

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
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should has where conditions");
        }
        StringBuilder sb = new StringBuilder(" SELECT ");
        sb.append(StringUtil.joinParams(",",params));
        sb.append(" FROM ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(builder);
        sb.append(" limit 1");
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return (T)AbstractSession.currentSession().findOne(this.clazz,sql,sqlParams);
    }

    public int count(){
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should has where conditions");
        }
        StringBuilder sb = new StringBuilder(" SELECT count(id) FROM ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(builder);
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return (int) AbstractSession.currentSession().findOne(Integer.class,sql,sqlParams);
    }





    public List<T> selectByPagination(Pagination pagination,String... params){
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should has where conditions");
        }
        StringBuilder sb = new StringBuilder(" SELECT count(id) FROM ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(builder);
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        int total = (int) AbstractSession.currentSession().findOne(Integer.class,sb.toString(),sqlParams);
        pagination.setTotal(total);


        StringBuilder list = new StringBuilder(" SELECT ");
        list.append(StringUtil.joinParams(",",params));
        list.append(" FROM ");
        list.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
        list.append(builder);
        list.append(" limit ?,? ");
        parameterBindings.get().addIndexBinding(pagination.getOffset());
        parameterBindings.get().addIndexBinding(pagination.getSize());
        Object[] listParams =parameterBindings.get().getIndexParametersArray();
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return (List<T>) AbstractSession.currentSession().findList(this.clazz, list.toString(), listParams);

    }



    public List<T> selectList(String... params){
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should has where conditions");
        }
        StringBuilder sb = new StringBuilder(" SELECT ");
        sb.append(StringUtil.joinParams(",",params));
        sb.append(" FROM ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(builder);
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return (List<T>) AbstractSession.currentSession().findList(this.clazz, sql, sqlParams);

    }

    public  int set(Object... params) {
        if(params==null||params.length==0){
            throw new JdbcRuntimeException("Error update set");
        }

        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should have where conditions");
        }
        ParameterBindings pb = new ParameterBindings();
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
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
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return getSession().execute(sql,all);
    }

    public  int set(Expr... exprs) {
        if(exprs==null||exprs.length==0){
            throw new JdbcRuntimeException("Error update set");
        }

        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should have where conditions");
        }
        ParameterBindings pb = new ParameterBindings();
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
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
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return getSession().execute(sql,all);
    }

    public  int set(Map<String,Object> map) {
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should have where conditions");
        }
        ParameterBindings pb = new ParameterBindings();
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
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
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return getSession().execute(sql,all);
    }


    public int set(String setSql,ParameterBindings setParams){
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should have where conditions");
        }
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(" SET ");
        sb.append(setSql);
        sb.append(builder);
        String sql = sb.toString();
        ParameterBindings all  ;
        if(setParams==null){
            setParams = new ParameterBindings();
        }
        all= setParams.addAll(parameterBindings.get());

        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return getSession().execute(sql,all);

    }


    public  int remove() {
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should have where conditions");
        }
        StringBuilder sb = new StringBuilder(" DELETE FROM ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(builder);
        String sql = sb.toString();
        Object[] params = parameterBindings.get().getIndexParametersArray();
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return getSession().execute(sql,params);
    }

    /**
     * get auto_increment_id ,when DRDS it get Sequence
     * @return
     */
    public long getDRDSAutoId(){
        Session session=getSession();
        ModelMeta modelMeta= session.getEntityMetaOfClass(this.clazz);
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
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should has where conditions");
        }
        StringBuilder sb = new StringBuilder(" SELECT 1 FROM ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(builder);
        sb.append(" limit 1");
        String sql= sb.toString();
        Object[] sqlParams =parameterBindings.get().getIndexParametersArray();
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        return AbstractSession.currentSession().findOne(Integer.class,sql,sqlParams)!=null;
    }



    public Dao<T> and(String column,String op,Object value){
        where.get().append(" and ");
        where.get().append(column);
        where.get().append(op);
        where.get().append("?");
        parameterBindings.get().addIndexBinding(value);
        return this;
    }

    public Dao<T> eq(String column,Object value){
        return and(column,"=",value);
    }

    public Dao<T> lk(String colum,Object value){return and(colum," LIKE ",value);}

    public Dao<T> idEqual(Object value){
        return and("id","=",value);
    }

    public Dao<T> id(Object value){
        return and("id","=",value);
    }

    public Dao<T> state(Object value){
        return and("state","=",value);
    }

    public Dao<T> partitionId(Object value){return
            and(getSession().getEntityMetaOfClass(this.clazz).getPatitionColumn().columnName,"=",value);
    }




    public Dao<T> gt(String column,Object value){
        return and(column,">",value);
    }

    public Dao<T> lt(String column,Object value){
        return and(column,"<",value);
    }

    public Dao<T> ne(String column,Object value){
        return and(column,"<>",value);
    }

    public Dao<T> le(String column,Object value){
        return and(column,"<=",value);
    }

    public Dao<T> ge(String column,Object value){
        return and(column,">=",value);
    }




    public Dao<T> in(String column,Object value){
        return and(column," in ","("+value+")");
    }

    public Dao<T> notIn(String column,Object value){
        return and(column," not in ","("+value+")");
    }

    public Dao<T> limit(int limit){
        where.get().append(" limit ");
        parameterBindings.get().addIndexBinding(limit);
        return this;
    }

    public Dao<T> asc(String col){
        where.get().append(" ORDER BY ");
        where.get().append(col);
        where.get().append(" ASC ");
        return this;
    }


    public Dao<T> desc(String col){
        where.get().append(" ORDER BY ");
        where.get().append(col);
        where.get().append(" desc ");
        return this;
    }


    public  Dao<T> orderBy(String orderBy){
        where.get().append(" ORDER BY ");
        where.get().append(orderBy);
        return  this;
    }

    public Dao<T> offset(int offset){
        where.get().append(" offset ");
        parameterBindings.get().addIndexBinding(offset);
        return this;
    }

    public Dao<T> or(List<Expr> exprs){
        if(exprs==null||exprs.size()==0){
            return this;
        }
        Expr[] exprs1 = (Expr[])exprs.toArray();
        return this.or(exprs1);
    }

    public Dao<T> or(Expr... exprs){
        where.get().append(" and (");
        boolean init =true;
        for(Expr expr:exprs){
            if(init){
                init = false;
            }else{
                where.get().append(" or ");
            }
            where.get().append(expr.getSql());
            parameterBindings.get().addIndexBinding(expr.getRight());
        }
        where.get().append(")");
        return this;
    }


}
