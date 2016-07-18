package cn.superid.utils.id_generator.services;

import cn.superid.utils.id_generator.exceptions.IdGeneratorException;

import java.util.Map;

/**
 * 数据库路由服务
 * Created by zoowii on 2014/9/3.
 */
public interface IDbRouter {
    /**
     * 对多维分表的表的插入请求选择一台路由到的目的节点（服务器）
     *
     * @param tableName
     * @param record
     * @return
     */
    long route(final String tableName, final Map<String, Object> record) throws IdGeneratorException;
}
