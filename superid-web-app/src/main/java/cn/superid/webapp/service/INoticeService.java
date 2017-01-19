package cn.superid.webapp.service;


import cn.superid.webapp.controller.VO.InvitationVO;
import cn.superid.webapp.controller.VO.NoticeVO;
import cn.superid.webapp.model.NoticeEntity;
import org.apache.thrift.TException;

import java.util.List;

/**
 * Created by xmc1993 on 16/12/15.
 */
public interface INoticeService {

    List<InvitationVO> getInvitationList(long userId);

    /**
     * 搜索消息
     *
     * @param userId 用户Id
     * @param state  消息状态，可选
     * @param type   消息类型，可选
     * @return
     */
    List<NoticeVO> search(long userId, Integer state, Integer type) throws Exception;

    boolean atSomeone(long frRid, String roleName, long frUid, String userName, long toUid, long relateId, long atRole) throws TException;

    void atSomeone(long toUid, long fromUid, String fromUname, String fromRoleTitle, long relatedId, String relateName) throws Exception;

    void taskCreated() throws Exception;

    void taskUpdated() throws Exception;

    void taskBeDueToExpire() throws Exception;

    void taskOverDue() throws Exception;

    void allianceJoinSuccess() throws Exception;

    void allianceFriendApply() throws Exception;

    void allianceFriendApplyAccepted() throws Exception;

    void allianceInvitation(long toUid, long invitationId, long allianceId, String allianceName, long inviterId, String inviterName, String inviterRoleTitle) throws Exception;

    void affairJoinSuccess() throws Exception;

    void affairMoveApply() throws Exception;

    void affairMoveApplyAccepted() throws Exception;

    void affairMoveApplyRejected() throws Exception;

}
