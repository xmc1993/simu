package cn.superid.webapp.service;

import org.apache.thrift.TException;

/**
 * Created by xmc1993 on 16/12/15.
 */
public interface INoticeService {

    boolean atSomeone(long frRid, String name, long relateId, long atRole);

    boolean taskCreated();

    boolean taskUpdated();

    boolean taskBeDueToExpire();

    boolean taskOverDue(long toUid, String userName, long taskId, String taskName, int numOfdays) throws TException;

    boolean allianceJoinSuccess(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName) throws TException;

    boolean allianceFriendApply(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName) throws TException;

    boolean allianceFriendApplyAccepted(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName) throws TException;

    boolean affairJoinSuccess(long frRid, String roleName, long frUid, String userName, long toUid, long allianceId, String allianceName, long affairId, long affairName) throws TException;

    boolean affairMoveApply(long frRid, String roleName, long frUid, String userName, long toUid, long fromAffair, String fromAffairName, long toAffair, String toAffairName) throws TException;

    boolean affairMoveApplyAccepted(long toUid, long fromAffair, String fromAffairName, long toAffair, String toAffairName) throws TException;

    boolean affairMoveApplyRejected(long toUid, long fromAffair, String fromAffairName, long toAffair, String toAffairName, long managerUser, String managerUserName) throws TException;
}
