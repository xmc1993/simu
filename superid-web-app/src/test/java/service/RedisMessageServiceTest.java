package service;

import cn.superid.webapp.forms.Message;
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
        Message message = new Message();
        redisMessageService.sendMessage("room_message", message);
        System.out.println("---end---");
    }

    @Test
    public void testPublishJsonMessage(){
        Message message = new Message();
        message.setContent("test room message");
        redisMessageService.sendJsonMessage("room_message", message);
        System.out.println("---end---");
    }
}
