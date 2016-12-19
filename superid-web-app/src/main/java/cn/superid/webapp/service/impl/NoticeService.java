package cn.superid.webapp.service.impl;

import cn.superid.webapp.enums.SystemEventType;
import cn.superid.webapp.notice.SendMessageTemplate;
import cn.superid.webapp.notice.thrift.S2c;
import cn.superid.webapp.service.INoticeService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * Created by xmc1993 on 16/12/16.
 */
@Service
public class NoticeService implements INoticeService {
    @Override
    public boolean atSomeone(long frRid, String name, long relateId, long atRole) {
        return false;
    }

    @Override
    public boolean taskCreated() {
        return false;
    }

    @Override
    public boolean taskUpdated() {
        return false;
    }

    @Override
    public boolean taskBeDueToExpire() {
        return false;
    }

    @Override
    public boolean taskOverDue(long toUid, String userName, long taskId, String taskName, int numOfdays) throws TException {
        String msg = taskName + "还有" + numOfdays + "天到期,去处理。";
        S2c s2c = new S2c();
        s2c.setSub(SystemEventType.TASK_OVERDUE);
        s2c.setContent(msg);
        s2c.setToUid(toUid);
        s2c.setTid(taskId);
        return SendMessageTemplate.sendNotice(s2c);
    }

    @Override
    public boolean allianceJoinSuccess(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName) throws TException {

        String msg = roleName + userName + "已经将你加为" + allianceName + "的成员";
        S2c s2c = new S2c();
        s2c.setSub(SystemEventType.ALLIANCE_JOIN_SUCCESS);
        s2c.setContent(msg);
        s2c.setFrRid(frRid);
        s2c.setFrUid(frUid);
        s2c.setToUid(toUid);
        s2c.setAllId(allianceId);
        return SendMessageTemplate.sendNotice(s2c);
    }

    @Override
    public boolean allianceFriendApply(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName) throws TException {
        String msg = allianceName + roleName + userName + "申请加您为好友,立即处理。";
        S2c s2c = new S2c();
        s2c.setSub(SystemEventType.ALLIANCE_FRIEND_APPLY);
        s2c.setContent(msg);
        s2c.setFrRid(frRid);
        s2c.setFrUid(frUid);
        s2c.setToUid(toUid);
        s2c.setAllId(allianceId);
        return SendMessageTemplate.sendNotice(s2c);
    }

    @Override
    public boolean allianceFriendApplyAccepted(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName) throws TException {
        String msg = allianceName + roleName + userName + "通过您的好友申请,你们已经是盟友了!";
        S2c s2c = new S2c();
        s2c.setSub(SystemEventType.ALLIANCE_FRIEND_APPLY_ACCEPTED);
        s2c.setContent(msg);
        s2c.setToUid(toUid);
        s2c.setFrRid(frRid);
        s2c.setFrUid(frUid);
        s2c.setAllId(allianceId);
        return SendMessageTemplate.sendNotice(s2c);
    }

    @Override
    public boolean affairJoinSuccess(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName, long affairId, long affairName) throws TException {
        String msg = roleName + userName + "已经将你加为" + allianceName + affairName + "的成员。";
        S2c s2c = new S2c();
        s2c.setSub(SystemEventType.AFFAIR_JOIN_SUCCESS);
        s2c.setContent(msg);
        s2c.setFrUid(frUid);
        s2c.setFrRid(frRid);
        s2c.setToUid(toUid);
        s2c.setAllId(allianceId);
        s2c.setAid(affairId);
        return SendMessageTemplate.sendNotice(s2c);
    }

    @Override
    public boolean affairMoveApply(long frRid, String roleName, long frUid, String userName, long toUid, long fromAffair, String fromAffairName, long toAffair, String toAffairName) throws TException {
        String msg = roleName + userName + "申请将事务" + fromAffairName + "移动至事务" + toAffairName + "下,立即处理。";
        S2c s2c = new S2c();
        s2c.setSub(SystemEventType.AFFAIR_MOVE_APPLY);
        s2c.setContent(msg);
        s2c.setFrUid(frUid);
        s2c.setFrRid(frRid);
        s2c.setToUid(toUid);
        s2c.setAid(fromAffair);
        s2c.setToRid(toAffair);
        return SendMessageTemplate.sendNotice(s2c);
    }

    @Override
    public boolean affairMoveApplyAccepted(long toUid, long fromAffair, String fromAffairName, long toAffair, String toAffairName) throws TException {
        String msg = "您申请将事务" + fromAffairName + "移动至" + toAffairName + "已成功!";
        S2c s2c = new S2c();
        s2c.setSub(SystemEventType.AFFAIR_MOVE_APPLY_ACCEPTED);
        s2c.setContent(msg);
        s2c.setToUid(toUid);
        s2c.setAid(fromAffair);
        s2c.setToAid(toAffair);
        return SendMessageTemplate.sendNotice(s2c);
    }

    @Override
    public boolean affairMoveApplyRejected(long toUid, long fromAffair, String fromAffairName, long toAffair, String toAffairName, long managerUser, String managerUserName) throws TException {
        String msg = "您申请将事务" + fromAffairName + "移动至" + toAffairName + "没有通过审核,联系管理员" + managerUserName;
        S2c s2c = new S2c();
        s2c.setSub(SystemEventType.AFFAIR_MOVE_APPLY_REJECTED);
        s2c.setContent(msg);
        s2c.setToUid(toUid);
        s2c.setAid(fromAffair);
        s2c.setToAid(toAffair);
        s2c.setRid(managerUser);//这边的管理员UserId为相关id
        return SendMessageTemplate.sendNotice(s2c);
    }
}
