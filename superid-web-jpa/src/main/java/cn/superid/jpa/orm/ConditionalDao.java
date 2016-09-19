package cn.superid.jpa.orm;

import cn.superid.jpa.util.Expr;

import java.util.List;

/**
 * Created by xiaofengxu on 16/9/18.
 */
public class ConditionalDao<T> extends Dao<T> {


    public ConditionalDao(Class clazz){
        this.clazz =clazz;
    }
    public ConditionalDao<T> and(String column, String op, Object value){
        where.get().append(" and ");
        where.get().append(column);
        where.get().append(op);
        where.get().append("?");
        parameterBindings.get().addIndexBinding(value);
        return this;
    }

    public ConditionalDao<T>  join (ConditionalDao table){
        from.get().append(getSession().getEntityMetaOfClass(this.clazz).getTableName() + " a");
        from.get().append(" join ");
        from.get().append(getSession().getEntityMetaOfClass(table.clazz).getTableName() + " b");
        return this;
    }

    public ConditionalDao<T> on (String leftParam , String rightParam ){
        from.get().append(" on ");
        from.get().append("a."+leftParam);
        from.get().append(" = ");
        from.get().append("b."+rightParam);
        return this;

    }

    public ConditionalDao<T> eq(String column, Object value){
        return and(column,"=",value);
    }

    public ConditionalDao<T> lk(String colum, Object value){return and(colum," LIKE ",value);}

    public ConditionalDao<T> idEqual(Object value){
        return and("id","=",value);
    }

    public ConditionalDao<T> id(Object value){
        return and("id","=",value);
    }

    public ConditionalDao<T> state(Object value){
        return and("state","=",value);
    }

    public ConditionalDao<T> partitionId(Object value){return
            and(getSession().getEntityMetaOfClass(this.clazz).getPatitionColumn().columnName,"=",value);
    }


    public ConditionalDao<T> gt(String column, Object value){
        return and(column,">",value);
    }

    public ConditionalDao<T> lt(String column, Object value){
        return and(column,"<",value);
    }

    public ConditionalDao<T> ne(String column, Object value){
        return and(column,"<>",value);
    }

    public ConditionalDao<T> le(String column, Object value){
        return and(column,"<=",value);
    }

    public ConditionalDao<T> ge(String column, Object value){
        return and(column,">=",value);
    }




    public ConditionalDao<T> in(String column, Object value){
        return and(column," in ","("+value+")");
    }

    public ConditionalDao<T> notIn(String column, Object value){
        return and(column," not in ","("+value+")");
    }

    public ConditionalDao<T> limit(int limit){
        where.get().append(" limit ");
        parameterBindings.get().addIndexBinding(limit);
        return this;
    }

    public ConditionalDao<T> asc(String col){
        where.get().append(" ORDER BY ");
        where.get().append(col);
        where.get().append(" ASC ");
        return this;
    }


    public ConditionalDao<T> desc(String col){
        where.get().append(" ORDER BY ");
        where.get().append(col);
        where.get().append(" desc ");
        return this;
    }


    public ConditionalDao<T> orderBy(String orderBy){
        where.get().append(" ORDER BY ");
        where.get().append(orderBy);
        return  this;
    }

    public ConditionalDao<T> groupBy(String groupBy){
        where.get().append(" GROUP BY ");
        where.get().append(groupBy);
        return  this;
    }

    public ConditionalDao<T> offset(int offset){
        where.get().append(" offset ");
        parameterBindings.get().addIndexBinding(offset);
        return this;
    }

    public ConditionalDao<T> or(List<Expr> exprs){
        if(exprs==null||exprs.size()==0){
            return this;
        }
        Expr[] exprs1 = (Expr[])exprs.toArray();
        return this.or(exprs1);
    }

    public ConditionalDao<T> or(Expr... exprs){
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
