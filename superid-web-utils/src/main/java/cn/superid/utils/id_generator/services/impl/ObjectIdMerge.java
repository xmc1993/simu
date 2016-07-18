package cn.superid.utils.id_generator.services.impl;

import cn.superid.utils.id_generator.services.IIdMerger;
import cn.superid.utils.ConfigUtil;
import cn.superid.utils.ObjectId;
import com.google.common.base.Function;

import java.util.Random;

/**
 * Created by zoowii on 2014/9/9.
 */
public class ObjectIdMerge implements IIdMerger {
    private static final Random random = new Random();

    @Override
    public String generateFinalId(Function<Void, Long> uniqueIdGenerator, String group, long serverId) {
        ObjectId objectId = new ObjectId((int) serverId, ConfigUtil.getIntConfig("server.id", random.nextInt()));
        return objectId.toString();
    }
}
