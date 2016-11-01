package cn.superid.webapp.service;

import cn.superid.webapp.model.FriendsApplicationEntity;
import cn.superid.webapp.model.UserEntity;

import java.util.List;

/**
 * Created by njuTms on 16/10/31.
 */
public interface IFriendsService {
    /**
     * 发送好友申请
     * @param toUserId
     * @param applyReason
     * @return
     */
    public boolean applyForFriend(long toUserId,String applyReason);

    public boolean applyForFriend(long fromUserId, long toUserId,String applyReason);

    /**
     * 同意好友申请
     * @param friendApplicationId
     * @param dealUserId
     * @param dealReason
     * @return
     */
    public boolean acceptFriendApplication(long friendApplicationId,long dealUserId,String dealReason);

    /**
     * 拒绝好友申请
     * @param friendApplicationId
     * @param dealUserId
     * @param dealReason
     * @return
     */
    public boolean rejectFriendApplication(long friendApplicationId,long dealUserId,String dealReason);


    /**
     * 查看自己发送过的所有好友申请
     * @param userId
     * @return
     */
    public List<FriendsApplicationEntity> showAllFriendsApplications(long userId);

    /**
     * 获取自己的所有好友
     * @param userId
     * @return
     */
    public List<UserEntity> showAllFriends(long userId);

}
