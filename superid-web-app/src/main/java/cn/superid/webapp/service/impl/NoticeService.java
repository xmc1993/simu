package cn.superid.webapp.service.impl;

import cn.superid.webapp.controller.VO.InvitationVO;
import cn.superid.webapp.dao.IInvitationDao;
import cn.superid.webapp.enums.NoticeType;
import cn.superid.webapp.model.NoticeEntity;
import cn.superid.webapp.notice.NoticeGenerator;
import cn.superid.webapp.notice.SendMessageTemplate;
import cn.superid.webapp.notice.thrift.C2c;
import cn.superid.webapp.notice.thrift.Msg;
import cn.superid.webapp.service.INoticeService;
import cn.superid.webapp.service.IUserService;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xmc1993 on 16/12/16.
 */
@Service
public class NoticeService implements INoticeService {
    private static final int SYSTEM = 10;//系统通知(消息类型)
    private static final int MSG = 0;//消息(数据类型)
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IInvitationDao invitationDao;

    @Autowired
    private IUserService userService;

    @Override
    public List<InvitationVO> getInvitationList() {
        return invitationDao.getInvitationList(userService.currentUserId());
    }

    @Override
    public boolean atSomeone(long frRid, String roleName, long frUid, String userName, long toUid, long relateId, long atRole) throws TException {
        return false;
    }

    @Override
    public void atSomeone(long toUid, long fromUid, String fromUname, String fromRoleTitle, long relatedId, String relateName) throws Exception {
    }

    @Override
    public void taskCreated() {
    }

    @Override
    public void taskUpdated() {
    }

    @Override
    public void taskBeDueToExpire() {
    }

    @Override
    public void taskOverDue() throws Exception {
    }

    @Override
    public void allianceJoinSuccess() throws Exception {
    }

    @Override
    public void allianceFriendApply() throws Exception {
    }

    @Override
    public void allianceFriendApplyAccepted() throws Exception {
    }

    @Override
    public void allianceInvitation(long toUid, long invitationId, String allianceName, long inviterId, String inviterName, String inviterRoleTitle) throws Exception {
        NoticeEntity noticeEntity = NoticeGenerator.getAllianceInvitation(toUid, invitationId, allianceName, inviterId, inviterName, inviterRoleTitle);
        //noticeEntity.save();
        Msg msg = newMsg(toUid);
        C2c c2c = newC2c(msg, noticeEntity);
        SendMessageTemplate.sendNotice(c2c);
    }

    @Override
    public void affairJoinSuccess() throws Exception {
    }

    @Override
    public void affairMoveApply() throws Exception {
    }

    @Override
    public void affairMoveApplyAccepted() throws Exception {
    }

    @Override
    public void affairMoveApplyRejected() throws Exception {
    }

    @Override
    public List<NoticeEntity> search(Long userId, Short state, Integer type) {
        return null;
    }

    private C2c newC2c(Msg msg, NoticeEntity noticeEntity) throws Exception {
        C2c c2c = new C2c();
        c2c.setType(MSG);
        c2c.setChat(msg);
        c2c.setData(objectMapper.writeValueAsString(noticeEntity));
        return c2c;
    }

    private Msg newMsg(long toUid) {
        Msg msg = new Msg();
        msg.setType(SYSTEM);
        msg.setToUid(toUid);
        return msg;
    }

}
