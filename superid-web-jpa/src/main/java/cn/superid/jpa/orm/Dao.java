package cn.superid.jpa.orm;
import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.jpa.util.*;

import java.util.List;


/**
 * Created by zp on 2016/7/21.
 */
public class Dao<T> {
    public static Function<String,Boolean> log =null ;
    private final static String whereStr = " WHERE (1=1) ";
    private Class<?> clazz;
    private static short whereLength = (short)whereStr.length();
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
        where.get().append("and (");
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

    public static Object findById(Class<?> cls,Object id){
        return  getSession().find(cls,id);
    }

    public T selectOne(String... params){
        StringBuilder builder = where.get();
        if(builder.length()==whereLength){
            throw new JdbcRuntimeException("You should has where conditions");
        }
        StringBuilder sb = new StringBuilder(" SELECT ");
        sb.append(StringUtil.joinParams(",", params));
        sb.append(" FROM ");
        sb.append(getSession().getEntityMetaOfClass(this.clazz).getTableName());
        sb.append(builder);
        sb.append(" limit 1");
        T rs =(T) AbstractSession.currentSession().findOne(this.clazz,sb.toString(),parameterBindings.get().getIndexParametersArray());
        builder.delete(whereLength,builder.length()-1);
        parameterBindings.get().clear();
        return rs;
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
        List rs = AbstractSession.currentSession().findList(this.clazz, sb.toString(), parameterBindings.get().getIndexParametersArray());
        builder.delete(whereLength,builder.length()-1);
        parameterBindings.get().clear();
        return (List<T>)rs;
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
        int result= getSession().execute(sql,all);
        builder.delete(whereLength,builder.length()-1);
        parameterBindings.get().clear();
        return result;
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
        int result = getSession().execute(sql,parameterBindings.get());
        builder.delete(whereLength,builder.length()-1);
        parameterBindings.get().clear();
        return result;
    }


    public int execute(String sql,ParameterBindings parameterBindings1){
        return getSession().execute(sql,parameterBindings1);
    }






}
