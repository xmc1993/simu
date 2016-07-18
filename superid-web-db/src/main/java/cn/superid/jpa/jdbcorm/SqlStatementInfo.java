package cn.superid.jpa.jdbcorm;

import cn.superid.jpa.query.ParameterBindings;
import org.apache.commons.lang3.tuple.Pair;

/**
 *  on 15/1/27.
 */
public class SqlStatementInfo {

    private final Pair<String, ParameterBindings> pair;

    private SqlStatementInfo(Pair<String, ParameterBindings> pair) {
        this.pair = pair;
    }

    public static SqlStatementInfo of(String sql, ParameterBindings parameterBindings) {
        return new SqlStatementInfo(Pair.of(sql, parameterBindings));
    }

    public String getSql() {
        return pair.getLeft();
    }

    public ParameterBindings getParameterBindings() {
        return pair.getRight();
    }

    public ParameterBindings setParameterBindings(ParameterBindings value) {
        return pair.setValue(value);
    }
}
