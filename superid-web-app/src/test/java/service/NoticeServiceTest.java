package service;

import cn.superid.webapp.service.INoticeService;
import org.apache.thrift.TException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by xmc1993 on 16/12/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class NoticeServiceTest {
    @Autowired
    private INoticeService noticeService;

    @Test
    public void testTaskOverDue() throws TException {
//        boolean result = noticeService.taskOverDue(123L, "xmc", 456L, "xmc's task", 3);
//        System.out.println("The result is:" + result);
    }
}
