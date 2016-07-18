package cn.superid.webapp.utils;

import cn.superid.utils.MapUtil;
import com.google.common.base.Function;
import com.zoowii.jpa_utils.query.Expr;
import com.zoowii.jpa_utils.query.Query;
import org.apache.commons.lang3.tuple.Pair;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 维 on 2014/9/27.
 */
public class Paginator {
    private long page = 1;
    private long pageSize = 10;
    private long total = 0;
    private List<Pair<String, Boolean>> orders = new ArrayList<>();
    private List<Expr> expressions = new ArrayList<>();
    private Function<Query, Query> beforeQueryProcessor = null;
    private List<Object> parameters = new ArrayList<>();

    public Map<String, Object> asMap() {
        return MapUtil.hashmap("page", page, "pageSize", pageSize, "total", total);
    }

    public Paginator() {
    }

    public Paginator(HttpServletRequestWrapper request) {
        this.page = NumberUtils.tryParseInt(request.getParameter("page"), 1);
        this.pageSize = NumberUtils.tryParseInt(request.getParameter("page_size"), 20);
    }

    public Paginator(HttpServletRequest request) {
        this.page = NumberUtils.tryParseInt(request.getParameter("page"), 1);
        this.pageSize = NumberUtils.tryParseInt(request.getParameter("page_size"), 20);
    }

    public long skippedCount() {
        return (page - 1) * pageSize;
    }

    public int getIntSkippedCount() {
        return (int) skippedCount();
    }

    public Paginator addExpression(Expr expr) {
        expressions.add(expr);
        return this;
    }

    /**
     * ==
     *
     * @param name
     * @param value
     * @return
     */
    public Paginator eq(String name, Object value) {
        return addExpression(Expr.createEQ(name, value));
    }

    /**
     * >
     *
     * @param name
     * @param value
     * @return
     */
    public Paginator gt(String name, Object value) {
        return addExpression(Expr.createGT(name, value));
    }

    /**
     * <
     *
     * @param name
     * @param value
     * @return
     */
    public Paginator lt(String name, Object value) {
        return addExpression(Expr.createLT(name, value));
    }

    /**
     * !=
     *
     * @param name
     * @param value
     * @return
     */
    public Paginator ne(String name, Object value) {
        return addExpression(Expr.createNE(name, value));
    }

    /**
     * >=
     *
     * @param name
     * @param value
     * @return
     */
    public Paginator ge(String name, Object value) {
        return addExpression(Expr.createGE(name, value));
    }

    /**
     * <=
     *
     * @param name
     * @param value
     * @return
     */
    public Paginator le(String name, Object value) {
        return addExpression(Expr.createLE(name, value));
    }

    public Paginator like(String name, String value) {
        return addExpression(Expr.createLIKE(name, value));
    }

    public long getPageCount() {
        return 1 + (total - 1) / pageSize;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
        if (this.page <= 0) {
            this.page = 1;
        }
    }

    public long getPageSize() {
        return pageSize;
    }

    public int getIntPageSize() {
        return (int) getPageSize();
    }

    public int getIntTotal() {
        return (int) getTotal();
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        if (this.total < 0) {
            this.total = 0;
        }
    }

    public List<Pair<String, Boolean>> getOrders() {
        return orders;
    }

    public void setOrders(List<Pair<String, Boolean>> orders) {
        this.orders = orders;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public List<Expr> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expr> expressions) {
        this.expressions = expressions;
    }

    public Function<Query, Query> getBeforeQueryProcessor() {
        return beforeQueryProcessor;
    }

    public void setBeforeQueryProcessor(Function<Query, Query> beforeQueryProcessor) {
        this.beforeQueryProcessor = beforeQueryProcessor;
    }
}
