package service;

import cn.superid.webapp.service.IRedisMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by xmc1993 on 16/9/12.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class RedisMessageServiceTest {
    @Autowired
    IRedisMessageService redisMessageService;

    @Test
    public void testPublishMessage(){

    }
}
