package service;

import cn.superid.webapp.service.INoticeService;
import cn.superid.webapp.service.IUpdateChatCacheService;
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
    @Autowired
    private IUpdateChatCacheService updateChatCacheService;

    @Test
    public void testTaskOverDue() throws TException {
        boolean result = noticeService.taskOverDue(123L, "xmc", 456L, "xmc's task", 3);
        System.out.println("The result is:" + result);
    }

    @Test
    public void testDisableAffair() throws TException {
        boolean result = updateChatCacheService.disableAffair(123L);
        System.out.println("The result is:" + result);
    }

    @Test
    public void testAffairMoveApplyAccepted() throws TException {
        boolean res = noticeService.affairMoveApplyAccepted(1911L, 7620L, "", 0L, "");
        System.out.println(res);
    }
}

