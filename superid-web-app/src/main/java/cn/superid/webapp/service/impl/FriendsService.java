package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.Expr;
import cn.superid.webapp.model.FriendsApplicationEntity;
import cn.superid.webapp.model.FriendsEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.service.IFriendsService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by njuTms on 16/10/31.
 */
@Service
public class FriendsService implements IFriendsService{
    @Autowired
    private IUserService userService;

    @Override
    public boolean applyForFriend(long toUserId, String applyReason) {
        boolean isExist = UserEntity.dao.id(toUserId).exists();
        if(!isExist){
            return false;
        }

        boolean canApply = isValidApplication(userService.currentUserId(),toUserId);
        if(!canApply){
            return false;
        }
        FriendsApplicationEntity friendsApplicationEntity = new FriendsApplicationEntity();
        friendsApplicationEntity.setApplyReason(applyReason);
        friendsApplicationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        friendsApplicationEntity.setState(0);
        friendsApplicationEntity.setFromUserId(userService.currentUserId());
        friendsApplicationEntity.setToUserId(toUserId);
        friendsApplicationEntity.save();

        //TODO 发送消息通知被申请用户
        return true;
    }

    @Override
    public boolean applyForFriend(long fromUserId, long toUserId, String applyReason) {
        boolean isExist = UserEntity.dao.id(toUserId).exists();
        if(!isExist){
            return false;
        }
        boolean canApply = isValidApplication(fromUserId,toUserId);
        if(!canApply){
            return false;
        }
        FriendsApplicationEntity friendsApplicationEntity = new FriendsApplicationEntity();
        friendsApplicationEntity.setApplyReason(applyReason);
        friendsApplicationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        friendsApplicationEntity.setState(0);
        friendsApplicationEntity.setFromUserId(fromUserId);
        friendsApplicationEntity.setToUserId(toUserId);
        friendsApplicationEntity.save();
        return true;
    }

    @Override
    public boolean acceptFriendApplication(long friendApplicationId, long dealUserId, String dealReason) {
        //检测传过来的dealUserId是否正确或符合该条申请的目标用户
        FriendsApplicationEntity friendsApplicationEntity = FriendsApplicationEntity.dao.id(friendApplicationId).partitionId(dealUserId).selectOne("toUserId","fromUserId");
        if(friendsApplicationEntity == null){
            return false;
        }
        long toUserId = friendsApplicationEntity.getToUserId();
        if(!(toUserId == dealUserId)){
            return false;
        }

        int isUpdate = FriendsApplicationEntity.dao.id(friendApplicationId).partitionId(dealUserId).set("state",1,"dealReason",dealReason);
        if(isUpdate<1)
            return false;
        long fromUserId = friendsApplicationEntity.getFromUserId();

        FriendsEntity friendsEntity = new FriendsEntity();
        friendsEntity.setState(0);
        friendsEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        friendsEntity.setToUserId(dealUserId);
        friendsEntity.setFromUserId(fromUserId);
        friendsEntity.setFriendApplicationId(friendApplicationId);
        friendsEntity.save();
        //TODO 发送消息通知申请用户
        return true;
    }

    @Override
    public boolean rejectFriendApplication(long friendApplicationId, long dealUserId, String dealReason) {
        //检测传过来的dealUserId是否正确或符合该条申请的目标用户
        FriendsApplicationEntity friendsApplicationEntity = FriendsApplicationEntity.dao.id(friendApplicationId).partitionId(dealUserId).selectOne("toUserId");
        if(friendsApplicationEntity == null){
            return false;
        }
        long toUserId = friendsApplicationEntity.getToUserId();
        if(!(toUserId == dealUserId)){
            return false;
        }


        int isUpdate = FriendsApplicationEntity.dao.id(friendApplicationId).partitionId(dealUserId).set("state",2,"dealReason",dealReason);
        if(isUpdate<1)
            return false;
        //TODO 发送消息通知申请用户
        return true;
    }

    @Override
    public List<FriendsApplicationEntity> showAllFriendsApplications(long userId) {

        return null;
    }

    //TODO 不知道页面要显示什么,可能现在的返回数据格式不对
    @Override
    public List<UserEntity> showAllFriends(long userId) {
        return null;
    }

    public boolean isValidApplication(long fromUserId,long toUserId){
        //查看是否有待处理的或者已经接受的申请,如果有就不能再申请

        boolean hasApplied = FriendsApplicationEntity.dao.partitionId(toUserId).eq("fromUserId",fromUserId).or(Expr.eq("state",0),Expr.eq("state",1)).exists();
        //查看是否存在对方向我发起的申请
        boolean hasBeenApplied = FriendsApplicationEntity.dao.partitionId(fromUserId).eq("fromUserId",toUserId).or(Expr.eq("state",0),Expr.eq("state",1)).exists();
        if(hasApplied||hasBeenApplied){
            return false;
        }
        return true;
    }
}
