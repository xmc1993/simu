package cn.superid.webapp.service.impl;

import cn.superid.webapp.enums.NoticeType;
import cn.superid.webapp.notice.SendMessageTemplate;
import cn.superid.webapp.notice.thrift.C2c;
import cn.superid.webapp.notice.thrift.Msg;
import cn.superid.webapp.service.INoticeService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * Created by xmc1993 on 16/12/16.
 */
@Service
public class NoticeService implements INoticeService {
    private static final int SYSTEM_TYPE = 10;
    @Override
    public boolean atSomeone(long frRid, String roleName, long frUid, String userName, long toUid, long relateId, long atRole) throws TException {
        String msg = roleName + userName + "位置" + "@了你,点击查看:" + "";
        Msg _msg = new Msg();
        _msg.setSub(NoticeType.AT_MEMBER);
        _msg.setFrUid(frUid);
        _msg.setFrRid(frRid);
        _msg.setContent(msg);
        _msg.setToUid(toUid);
        _msg.setRid(relateId);
        return SendMessageTemplate.sendNotice(newC2c(_msg));
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
        Msg _msg = new Msg();
        _msg.setSub(NoticeType.TASK_OVERDUE);
        _msg.setContent(msg);
        _msg.setToUid(toUid);
        _msg.setRid(taskId);
        return SendMessageTemplate.sendNotice(newC2c(_msg));
    }

    @Override
    public boolean allianceJoinSuccess(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName) throws TException {

        String msg = roleName + userName + "已经将你加为" + allianceName + "的成员";
        Msg _msg = new Msg();
        _msg.setSub(NoticeType.ALLIANCE_JOIN_SUCCESS);
        _msg.setContent(msg);
        _msg.setFrRid(frRid);
        _msg.setFrUid(frUid);
        _msg.setToUid(toUid);
        _msg.setRid(allianceId); //盟id
        return SendMessageTemplate.sendNotice(newC2c(_msg));
    }

    @Override
    public boolean allianceFriendApply(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName) throws TException {
        String msg = allianceName + roleName + userName + "申请加您为好友,立即处理。";
        Msg _msg = new Msg();
        _msg.setSub(NoticeType.ALLIANCE_FRIEND_APPLY);
        _msg.setContent(msg);
        _msg.setFrRid(frRid);
        _msg.setFrUid(frUid);
        _msg.setToUid(toUid);
        _msg.setRid(allianceId); //盟id
        return SendMessageTemplate.sendNotice(newC2c(_msg));
    }

    @Override
    public boolean allianceFriendApplyAccepted(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName) throws TException {
        String msg = allianceName + roleName + userName + "通过您的好友申请,你们已经是盟友了!";
        Msg _msg = new Msg();
        _msg.setSub(NoticeType.ALLIANCE_FRIEND_APPLY_ACCEPTED);
        _msg.setContent(msg);
        _msg.setToUid(toUid);
        _msg.setFrRid(frRid);
        _msg.setFrUid(frUid);
        _msg.setRid(allianceId); //盟id
        return SendMessageTemplate.sendNotice(newC2c(_msg));
    }

    @Override
    public boolean affairJoinSuccess(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName, long affairId, long affairName) throws TException {
        String msg = roleName + userName + "已经将你加为" + allianceName + affairName + "的成员。";
        Msg _msg = new Msg();
        _msg.setSub(NoticeType.AFFAIR_JOIN_SUCCESS);
        _msg.setContent(msg);
        _msg.setFrUid(frUid);
        _msg.setFrRid(frRid);
        _msg.setToUid(toUid);
        _msg.setRid(allianceId);//盟id
        _msg.setAid(affairId);
        return SendMessageTemplate.sendNotice(newC2c(_msg));
    }

    @Override
    public boolean affairMoveApply(long frRid, String roleName, long frUid, String userName, long toUid, long fromAffair, String fromAffairName, long toAffair, String toAffairName) throws TException {
        String msg = roleName + userName + "申请将事务" + fromAffairName + "移动至事务" + toAffairName + "下,立即处理。";
        Msg _msg = new Msg();
        _msg.setSub(NoticeType.AFFAIR_MOVE_APPLY);
        _msg.setContent(msg);
        _msg.setFrUid(frUid);
        _msg.setFrRid(frRid);
        _msg.setToUid(toUid);
        _msg.setAid(fromAffair);
        _msg.setToRid(toAffair);
        return SendMessageTemplate.sendNotice(newC2c(_msg));
    }

    @Override
    public boolean affairMoveApplyAccepted(long toUid, long fromAffair, String fromAffairName, long toAffair, String toAffairName) throws TException {
        String msg = "您申请将事务" + fromAffairName + "移动至" + toAffairName + "已成功!";
        Msg _msg = new Msg();
        _msg.setSub(NoticeType.AFFAIR_MOVE_APPLY_ACCEPTED);
        _msg.setContent(msg);
        _msg.setToUid(toUid);
        _msg.setAid(fromAffair);
        _msg.setRid(toAffair);
        return SendMessageTemplate.sendNotice(newC2c(_msg));
    }

    @Override
    public boolean affairMoveApplyRejected(long toUid, long fromAffair, String fromAffairName, long toAffair, String toAffairName, long managerUser, String managerUserName) throws TException {
        String msg = "您申请将事务" + fromAffairName + "移动至" + toAffairName + "没有通过审核,联系管理员" + managerUserName;
        Msg _msg = new Msg();
        _msg.setSub(NoticeType.AFFAIR_MOVE_APPLY_REJECTED);
        _msg.setContent(msg);
        _msg.setToUid(toUid);
        _msg.setAid(fromAffair);
        _msg.setRid(toAffair);
        _msg.setRid(managerUser);//这边的管理员UserId为相关id
        return SendMessageTemplate.sendNotice(newC2c(_msg));
    }

    private C2c newC2c(Msg msg){
        C2c c2c = new C2c();
        c2c.setType(SYSTEM_TYPE);
        c2c.setChat(msg);
        return c2c;
    }
}
