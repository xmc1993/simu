package cn.superid.jpa.orm;

import cn.superid.jpa.util.Expr;
import cn.superid.jpa.util.StringUtil;

import java.util.List;

/**
 * Created by xiaofengxu on 16/9/18.
 */
public class ConditionalDao extends Dao{


    public ConditionalDao(Class clazz){
        this.clazz =clazz;
    }
    public ConditionalDao and(String column, String op, Object value){
        where.get().append(" and ");
        where.get().append(column);
        where.get().append(op);
        where.get().append("?");
        parameterBindings.get().addIndexBinding(value);
        return this;
    }

    public ConditionalDao  join (Class<?> table){
        from.get().append(ModelMeta.getModelMeta(this.clazz).getTableName() + " a");
        from.get().append(" join ");
        from.get().append(ModelMeta.getModelMeta(table).getTableName() + " b");
        return this;
    }

    public ConditionalDao on (String leftParam , String rightParam ){
        from.get().append(" on ");
        from.get().append("a."+leftParam);
        from.get().append(" = ");
        from.get().append("b."+rightParam);
        return this;

    }

    public ConditionalDao eq(String column, Object value){
        return and(StringUtil.underscoreName(column),"=",value);
    }

    public ConditionalDao neq(String column, Object value){
        return and(StringUtil.underscoreName(column),"<>",value);
    }

    public ConditionalDao lk(String colum, Object value){return and(colum," LIKE ",value);}


    public ConditionalDao id(Object value){
        return and("id","=",value);
    }
    public ConditionalDao key(Object value){
        return id(value);
    }

    public ConditionalDao state(Object value){
        return and("state","=",value);
    }

    public ConditionalDao partitionId(Object value){return
            and(ModelMeta.getModelMeta(this.clazz).getPartitionColumn().columnName,"=",value);
    }

    public ConditionalDao gt(String column, Object value){
        return and(column,">",value);
    }

    public ConditionalDao lt(String column, Object value){
        return and(column,"<",value);
    }

    public ConditionalDao ne(String column, Object value){
        return and(column,"<>",value);
    }

    public ConditionalDao le(String column, Object value){
        return and(column,"<=",value);
    }

    public ConditionalDao ge(String column, Object value){
        return and(column,">=",value);
    }

    public ConditionalDao isNull(String column){
        where.get().append(" and ");
        where.get().append(column);
        where.get().append(" is null ");
        return this;
    }

    public ConditionalDao isNotNull(String column){
        where.get().append(" and ");
        where.get().append(column);
        where.get().append(" is not null ");
        return this;
    }


    private ConditionalDao inOrNotIn(String column, Object[] values,String op){//
        where.get().append(" and ");
        where.get().append(column);
        where.get().append(op);
        where.get().append(" ( ");
        boolean first = true;
        for(Object value:values){
            if(first){
                where.get().append("?");
                first = false;
            }else {
                where.get().append(",?");
            }
            parameterBindings.get().addIndexBinding(value);
        }
        where.get().append(") ");
        return this;
    }


    public ConditionalDao in(String column, Object[] values){
       return inOrNotIn(StringUtil.underscoreName(column),values," in ");
    }

    public ConditionalDao notIn(String column, Object[] values){
        return inOrNotIn(StringUtil.underscoreName(column),values," not in ");
    }

    public ConditionalDao limit(int limit){
        where.get().append(" limit ");
        parameterBindings.get().addIndexBinding(limit);
        return this;
    }

    public ConditionalDao asc(String col){
        where.get().append(" ORDER BY ");
        where.get().append(StringUtil.underscoreName(col));
        where.get().append(" ASC ");
        return this;
    }


    public ConditionalDao desc(String col){
        where.get().append(" ORDER BY ");
        where.get().append(StringUtil.underscoreName(col));
        where.get().append(" desc ");
        return this;
    }


    public ConditionalDao orderBy(String orderBy){
        where.get().append(" ORDER BY ");
        where.get().append(StringUtil.underscoreName(orderBy));
        return  this;
    }

    public ConditionalDao groupBy(String groupBy){
        where.get().append(" GROUP BY ");
        where.get().append(StringUtil.underscoreName(groupBy));
        return  this;
    }

    public ConditionalDao offset(int offset){
        where.get().append(" offset ");
        parameterBindings.get().addIndexBinding(offset);
        return this;
    }

    public ConditionalDao or(List<Expr> exprs){
        if(exprs==null||exprs.size()==0){
            return this;
        }
        Expr[] exprs1 = (Expr[])exprs.toArray();
        return this.or(exprs1);
    }

    public ConditionalDao or(Expr... exprs){
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
