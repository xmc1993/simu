import cn.superid.webapp.model.TestEntity;
import cn.superid.webapp.utils.Timer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;
import org.springframework.test.context.ContextConfiguration;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Map;

/**
 * Created by xmc1993 on 16/9/8.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class TestSerializeCapability {

    TestEntity testEntity;
    Map<String, String> stringHashMap;
    public static Jedis jedisClient = new Jedis("localhost");

    public static BinaryClient binaryClient = new BinaryClient("localhost");


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Before
    public void before(){
        BeanUtilsHashMapper beanUtilsHashMapper = new BeanUtilsHashMapper(TestEntity.class);
        testEntity = new TestEntity();
        testEntity.setId(345L);
        testEntity.setDescription("test");
        testEntity.setCreateRoleId(123L);
        testEntity.setAllianceId(456L);
        testEntity.setIsFree(1);
        testEntity.setIsVideo(0);
        stringHashMap = beanUtilsHashMapper.toHash(testEntity);
    }

    @After
    public void after(){
        testEntity = null;
        stringHashMap = null;
    }

    @Test
    public void testObjectSerialize(){
        long beginTime = new Date().getTime();
        for(int i = 0; i < 1000; i++) {
            BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps(TestEntity.class.getName() + i);
            ops.putAll(stringHashMap);

        }
        long endTime = new Date().getTime();

        System.out.println("total time is: " + (endTime - beginTime) + "ms");
    }

    @Test
    public void testHashSerialize(){
        TestEntity testEntity=new TestEntity();
        testEntity = new TestEntity();
        testEntity.setId(345L);
        testEntity.setDescription("test");
        testEntity.setCreateRoleId(123L);
        testEntity.setAllianceId(456L);
        testEntity.setIsFree(1);
        testEntity.setIsVideo(0);

        Timer timer = new Timer();
        for(int i = 0; i < 1000; i++) {
            BeanUtilsHashMapper beanUtilsHashMapper = new BeanUtilsHashMapper(TestEntity.class);
            stringHashMap =beanUtilsHashMapper.toHash(testEntity);
        }
        timer.end();

        Timer timer1=new Timer();
        for(int i = 0; i < 1000; i++) {
            stringHashMap = testEntity.hashMap();
        }
        timer1.end();
    }









}
