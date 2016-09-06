import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.TestEntity;
import org.junit.Test;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Map;

/**
 * Created by xmc1993 on 16/9/2.
 */
public class TestRedisSerializer {

    @Test
    public void testJacksonSerializer(){
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer("cn.superid.webapp.model.TestEntity");
        TestEntity testEntity = new TestEntity();
        testEntity.setAllianceId(123L);
        testEntity.setCreateRoleId(456L);
        testEntity.setDescription("嘿嘿嘿");
        byte[] serialize = genericJackson2JsonRedisSerializer.serialize(testEntity);
        TestEntity deserialize = genericJackson2JsonRedisSerializer.deserialize(serialize, TestEntity.class);
        System.out.println("over");
    }

    @Test
    public void testJsonSerializer(){
        Jackson2JsonRedisSerializer<TestEntity> affairEntityJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<TestEntity>(TestEntity.class);
        TestEntity TestEntity = new TestEntity();
        TestEntity.setAllianceId(123L);
        TestEntity.setCreateRoleId(456L);
        TestEntity.setDescription("嘿嘿嘿");
        byte[] serialize = affairEntityJackson2JsonRedisSerializer.serialize(TestEntity);
        TestEntity deserialize = affairEntityJackson2JsonRedisSerializer.deserialize(serialize);
        System.out.println("over");
    }

    @Test
    public void testHashMapperUtil(){
        BeanUtilsHashMapper beanUtilsHashMapper = new BeanUtilsHashMapper(TestEntity.class);
        TestEntity testEntity = new TestEntity();
        testEntity.setAllianceId(123L);
        testEntity.setCreateRoleId(456L);
        testEntity.setDescription("嘿嘿嘿");
        Map map = beanUtilsHashMapper.toHash(testEntity);
        System.out.println("over");
    }


    @Test
    public void testGetClass(){
        Integer a = 0;
        System.out.println(a.getClass());
    }
}

