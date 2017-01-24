package service;

import cn.superid.webapp.model.InvitationEntity;
import cn.superid.webapp.model.NoticeEntity;
import cn.superid.webapp.notice.Link;
import cn.superid.webapp.notice.NoticeGenerator;
import cn.superid.webapp.service.INoticeService;
import cn.superid.webapp.service.IUpdateChatCacheService;
import org.apache.thrift.TException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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
    public void testNoticeSearch() throws Exception {
        List<NoticeEntity> noticeVOList = noticeService.search(1912l, 0, 2);
        System.out.println(noticeVOList);
    }

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
        InvitationEntity invitationEntity = InvitationEntity.dao.findById(1,2397);
        noticeService.allianceInvitation(invitationEntity);

        InvitationEntity invitation1 = InvitationEntity.dao.findById(5,2397);
        noticeService.affairInvitation(invitation1);

    }


}

