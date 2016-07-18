package cn.superid.utils.id_generator.workers;

import cn.superid.utils.id_generator.exceptions.IdGeneratorException;

import java.util.Map;

/**
 * 用来产生ID的服务
 * Created by zoowii on 2014/8/29.
 */
public interface IIdGeneratorWorker {
    String nextId(final String tableName, final Map<String, Object> record) throws IdGeneratorException;

    String nextId(String group) throws IdGeneratorException;
}
