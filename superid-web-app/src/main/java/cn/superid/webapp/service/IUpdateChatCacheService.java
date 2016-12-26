package cn.superid.webapp.service;

import org.apache.thrift.TException;

/**
 * Created by xmc1993 on 16/12/15.
 */
public interface IUpdateChatCacheService {

    /**
     * 解散群组
     * @param affairId 事务编号
     * @param groupId 群组编号
     * @return
     */
    boolean unGroup(long affairId, long groupId) throws TException;

    /**
     * 从群组中移除成员
     * @param affairId 事务编号
     * @param groupId 群组编号
     * @param roleId 角色号
     * @return
     */
    boolean removeMemberFromGroup(long affairId, long groupId, long roleId) throws TException;

    /**
     * 向群组中添加成员
     * @param affairId 事务编号
     * @param groupId 群组编号
     * @param roleId 角色号
     * @return
     */
    boolean addMemberToGroup(long affairId, long groupId, long roleId) throws TException;


}
