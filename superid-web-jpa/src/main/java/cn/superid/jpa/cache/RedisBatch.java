package cn.superid.jpa.cache;

import org.apache.commons.collections.map.HashedMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaofengxu on 17/1/23.
 */
public class RedisBatch {
    private Jedis jedis;
    private Pipeline pipeline;
    private Map<Object,Object>  map = new HashedMap();
    private List<Params> paramsList = new ArrayList<>();

    private class Params{
        public Class cls;
        public Object id;
        public String[] fields;

        public Params(Class cls, Object id, String[] fields) {
            this.cls = cls;
            this.id = id;
            this.fields = fields;
        }
    }

    public RedisBatch() {
        this.jedis = RedisTemplate.getJedis();
        this.pipeline = jedis.pipelined();
    }

    public void add(Class cls, Object id, String... fields){
        paramsList.add(new Params(cls,id,fields));
        if(fields==null){
//            pipeline.hgetAll(RedisTemplate.generateKey())
        }
    }
}
