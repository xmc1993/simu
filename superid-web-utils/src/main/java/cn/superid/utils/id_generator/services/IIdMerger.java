package cn.superid.utils.id_generator.services;

import com.google.common.base.Function;

/**
 * 将用来生成最终ID的各部分组合成最终的ID
 * Created by zoowii on 2014/8/29.
 */
public interface IIdMerger {
    /**
     * ID组成方式：
     * 64bit，前48bit存储本身就唯一的ID，从数据库服务器中序列产生，后16bit存储在该group中的机器编号
     * @param uniqueIdGenerator
     * @param group
     * @param serverId
     * @return
     */
    String generateFinalId(Function<Void, Long> uniqueIdGenerator, String group, long serverId);
}
