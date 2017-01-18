package service;

import cn.superid.webapp.service.INoticeService;
import cn.superid.webapp.service.IUpdateChatCacheService;
import org.apache.thrift.TException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

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
    }

    @Test
    public void testDisableAffair() throws TException {
    }

    @Test
    public void testAffairMoveApplyAccepted() throws TException {
    }

    @Test
    public void testAllianceInvitation() throws Exception {
        while (true) {
            noticeService.allianceInvitation(1912, 1, 111, "test", 2400, "cdd", "manager");
            Thread.sleep(5000);
        }
    }


}

