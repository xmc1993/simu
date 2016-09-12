import cn.superid.webapp.model.TestEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;
import org.springframework.test.context.ContextConfiguration;

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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


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
        for(int i = 0; i < 100; i++){
//            BoundValueOperations<String, String> ops = stringRedisTemplate.boundValueOps("dd");


        }
    }




}
