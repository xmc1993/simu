package cn.superid.jpa.query;

import cn.superid.jpa.jdbcorm.sqlmapper.SqlMapper;
import cn.superid.jpa.query.*;
import cn.superid.jpa.query.Expr;

import java.util.List;

public class AndExpr extends cn.superid.jpa.query.Expr {
    public AndExpr() {
    }

    public AndExpr(String op, List<Object> items) {
        super(op, items);
    }

    @Override
    public QueryInfo toQueryString(SqlMapper sqlMapper, cn.superid.jpa.query.Query query) {
        cn.superid.jpa.query.Expr left = (cn.superid.jpa.query.Expr) items.get(0);
        cn.superid.jpa.query.Expr right = (Expr) items.get(1);
        QueryInfo leftQuery = left.toQueryString(sqlMapper, query);
        QueryInfo rightQuery = right.toQueryString(sqlMapper, query);
        String queryStr = "(" + leftQuery.getQueryString() + " " + op + " " + rightQuery.getQueryString() + ")";
        ParameterBindings bindings = new ParameterBindings();
        bindings = bindings.addAll(leftQuery.getParameterBindings());
        bindings = bindings.addAll(rightQuery.getParameterBindings());
        return new QueryInfo(queryStr, bindings);
    }
}
