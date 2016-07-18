package cn.superid.utils.id_generator.services.impl;

import cn.superid.utils.id_generator.services.IIdMerger;
import com.google.common.base.Function;

/**
 * Created by zoowii on 2014/8/29.
 */
public class IdMerge implements IIdMerger {
    @Override
    public String generateFinalId(Function<Void, Long> uniqueIdGenerator, String group, long serverId) {
        if (uniqueIdGenerator == null) {
            return null;
        }
        Long uniqueIdObj = uniqueIdGenerator.apply(null);
        if(uniqueIdObj == null) {
            return null;
        }
        long uniqueId = uniqueIdObj;
        long secondPart = (serverId << 48) >> 48;
        long firstPart = (uniqueId << 32) >> 16;
        return firstPart + secondPart + "";
    }
}
