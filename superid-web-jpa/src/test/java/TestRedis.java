import cn.superid.jpa.cache.RedisTemplate;
import cn.superid.jpa.orm.ModelMeta;
import cn.superid.jpa.util.BinaryUtil;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import model.BaseUser;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaofengxu on 16/9/21.
 */

public class TestRedis {
    static {
        new InitResource();
    }
    private final static Codec<BaseUser> codec = ProtobufProxy
            .create(BaseUser.class);
    @Test
    public void testHmset(){
        final BaseUser user = new BaseUser();
//        user.setName("zp");
//        user.save();
//        String res = RedisTemplate.save(user);


        ModelMeta modelMeta = ModelMeta.getModelMeta(BaseUser.class);
        byte[] redisKey = RedisTemplate.generateKey(modelMeta.getKey(), BinaryUtil.getBytes(1036));
//        long a = RedisTemplate.delete(redisKey);
//        System.out.print(a);


        Jedis jedis =RedisTemplate.getJedis();
        jedis.del(redisKey);

//        BaseUser.dao.findById(1036);

        Map<byte[], byte[]> map = jedis.hgetAll(redisKey);


        for(byte[] key:map.keySet()){
            System.out.println(BinaryUtil.toString(key) +"--"+BinaryUtil.toString(map.get(key)));
        }


        BaseUser.dao.id(1036).set("name","zp");

        map = jedis.hgetAll(redisKey);
        for(byte[] key:map.keySet()){
            System.out.println(BinaryUtil.toString(key) +"--"+BinaryUtil.toString(map.get(key)));
        }

//        Long a = RedisTemplate.delete(RedisTemplate.generateKey())
//        user.setName("zphahah");
//        user.setAge(19);
//        user.save();
//
//        String result =(String) BaseUser.dao.findFieldByKey(user.getId(),"name",String.class);
//        Assert.assertTrue(result.equals(user.getName()));
//
//        BaseUser user1 = BaseUser.dao.id(user.getId()).selectOne("name","age");
//        Assert.assertTrue(user1.getAge()==19);

    }

    @Test//as result,the spend time almost equal;but the memory,save as hashmap,the use-memory-human is 988kb to 2.31M,and save as serialize,988kb to 3.37M
    public void testRedisPoJO() {
        final BaseUser user = new BaseUser();
        user.setName("zphahah");
        user.setAge(19);
        user.setId(1000);
        Timer.compair(new Execution() {
            @Override
            public void execute() {
                user.setId(user.getId()+1);
                RedisTemplate.save(user);
//                BaseUser baseUser= BaseUser.dao.id(user.getId()).selectOne("name");
//                String name = baseUser.getName();

            }
        }, new Execution() {
            @Override
            public void execute(){
                user.setId(user.getId()+1);
                Jedis jedis= RedisTemplate.getJedis();
                try {
                    jedis.set(("test"+user.getId()).getBytes(),codec.encode(user));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                jedis.close();

//                jedis= RedisTemplate.getJedis();
//                byte[] bytes = jedis.get(("user"+user.getId()).getBytes());
//                try {
//                    BaseUser baseUser = codec.decode(bytes);
//                    String name = baseUser.getName();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                jedis.close();
            }
        },10000);

    }


    @Test//as result,the spend time almost equal;but the memory,save as hashmap,the use-memory-human is 988kb to 2.31M,and save as serialize,988kb to 3.37M
    public void testRedisPipeline() {
        Timer timer = new Timer();
        final BaseUser user = new BaseUser();
        user.setName("zphahah");
        user.setAge(19);
        user.setId(11000);
        Jedis jedis= RedisTemplate.getJedis();
        Pipeline pipeline=jedis.pipelined();

        for(int i=0;i<10000;i++){
            user.setId(user.getId()+1);
            pipeline.exists(("test"+user.getId()).getBytes());
        }
        List<Object> booleen =pipeline.syncAndReturnAll();

        jedis.close();
        timer.end();

    }





}
