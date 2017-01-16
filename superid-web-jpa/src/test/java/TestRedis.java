import cn.superid.jpa.cache.impl.RedisTemplate;
import cn.superid.jpa.orm.ModelMeta;
import cn.superid.jpa.util.BinaryUtil;
import model.BaseUser;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by xiaofengxu on 16/9/21.
 */

public class TestRedis {
    static {
        new InitResource();
    }
    @Test
    public void testHmset(){
        final BaseUser user = new BaseUser();
//        user.setName("zp");
//        user.save();
//        String res = RedisTemplate.save(user);


        ModelMeta modelMeta = ModelMeta.getModelMeta(BaseUser.class);
        byte[] redisKey = RedisTemplate.generateKey(modelMeta.getKey(), BinaryUtil.getBytes(100000));
        long a = RedisTemplate.delete(redisKey);
        System.out.print(a);


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
    public void testRedisPoJO(){
        final BaseUser user = new BaseUser();
        user.setName("zphahah");
        user.setAge(19);
        user.setId(1);
        Timer.compair(new Execution() {
            @Override
            public void execute() {
                user.setId(user.getId()+1);
                RedisTemplate.save(user);
                BaseUser user1=(BaseUser) RedisTemplate.findByKey(user.getId(),user.getClass());
                Assert.assertTrue(user1.getName().equals(user.getName()));

            }
        }, new Execution() {
            @Override
            public void execute() {
                user.setId(user.getId()+1);
                Jedis jedis= RedisTemplate.getJedis();
                jedis.set(("user"+user.getId()).getBytes(),SerializeUtil.serialize(user));
                jedis.close();
                jedis = RedisTemplate.getJedis();
                 BaseUser user1 =(BaseUser) SerializeUtil.unserialize(jedis.get(("user"+user.getId()).getBytes()));
                jedis.close();
                Assert.assertTrue(user1.getName().equals(user.getName()));
            }
        },100);

    }








}
