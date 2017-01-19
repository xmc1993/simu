package cn.superid.webapp.service.impl;

import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.utils.Converter;
import cn.superid.webapp.controller.VO.InvitationVO;
import cn.superid.webapp.controller.VO.NoticeVO;
import cn.superid.webapp.dao.IInvitationDao;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.model.NoticeEntity;
import cn.superid.webapp.notice.Link;
import cn.superid.webapp.notice.NoticeGenerator;
import cn.superid.webapp.notice.SendMessageTemplate;
import cn.superid.webapp.notice.chat.Constant.C2CType;
import cn.superid.webapp.notice.thrift.C2c;
import cn.superid.webapp.service.INoticeService;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xmc1993 on 16/12/16.
 */
@Service
public class NoticeService implements INoticeService {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IInvitationDao invitationDao;

    @Override
    public List<InvitationVO> getInvitationList(long userId) {
        return invitationDao.getInvitationList(userId);
    }

    @Override
    public List<NoticeVO> search(long userId, Integer state, Integer type) throws Exception {
        ConditionalDao conditionalDao = NoticeEntity.dao.eq("userId", userId);
        if (state != null)
            conditionalDao = conditionalDao.eq("state", state);
        if (type != null) {
            if (type == 1)//@我，见 NoticeType 类
                conditionalDao = conditionalDao.eq("type", 10);
            if (type == 2)//任务相关
                conditionalDao = conditionalDao.ge("type", 20).le("type", 49);
            if (type == 3)//系统
                conditionalDao = conditionalDao.ge("type", 50).le("type", 99);
        }
        List<NoticeEntity> noticeEntities = conditionalDao.selectList(NoticeEntity.class);
        List<NoticeVO> voList = new ArrayList<>(noticeEntities.size());
        for (NoticeEntity noticeEntity : noticeEntities) {
            NoticeVO noticeVO = Converter.convert(NoticeVO.class, noticeEntity);
            noticeVO.setUrls(objectMapper.readValue(noticeEntity.getUrls(), Link[].class));
            voList.add(noticeVO);
        }
        return voList;
    }

    @Override
    public boolean markAsRead(long id) {
        NoticeEntity noticeEntity = NoticeEntity.dao.findById(id);
        noticeEntity.setState(ValidState.Invalid);
        noticeEntity.save();
        return true;
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
    public void allianceInvitation(long toUid, long invitationId, long allianceId, String allianceName, long inviterId, String inviterName, String inviterRoleTitle) throws Exception {
        NoticeVO noticeVO = NoticeGenerator.getAllianceInvitation(toUid, invitationId, allianceId, allianceName, inviterId, inviterName, inviterRoleTitle);
        C2c c2c = newC2c(toUid, noticeVO);
        saveNoticeEntity(noticeVO);
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

    private void saveNoticeEntity(NoticeVO noticeVO) throws Exception {
        NoticeEntity noticeEntity = Converter.convert(NoticeEntity.class, noticeVO);
        noticeEntity.setUrls(objectMapper.writeValueAsString(noticeVO.getUrls()));
        noticeEntity.save();
        noticeVO.setId(noticeEntity.getId());
    }

    private C2c newC2c(long toUid, NoticeVO noticeVO) throws Exception {
        C2c c2c = new C2c();
        c2c.setType(C2CType.SYSTEM_NOTICE);
        c2c.setParams(Long.toString(toUid));
        c2c.setData(objectMapper.writeValueAsString(noticeVO));
        return c2c;
    }

    private String join(long[] ids) {
        StringBuilder sb = new StringBuilder();
        for (long id : ids) {
            sb.append(id).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
