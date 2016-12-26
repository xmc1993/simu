package cn.superid.webapp.service.impl;

import cn.superid.webapp.enums.CacheUpdateEventType;
import cn.superid.webapp.notice.ParamVo;
import cn.superid.webapp.notice.SendMessageTemplate;
import cn.superid.webapp.notice.thrift.C2c;
import cn.superid.webapp.service.IUpdateChatCacheService;
import com.google.gson.Gson;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * Created by xmc1993 on 16/12/16.
 */
@Service
public class UpdateChatCacheService implements IUpdateChatCacheService{
    private static final int UPDATE_CACHE = 13;//更新缓存(数据类型)

    @Override
    public boolean unGroup(long affairId, long groupId) throws TException {
        ParamVo params = new ParamVo();
        params.setAffairId(affairId);
        params.setGroupId(groupId);
        params.setEventType(CacheUpdateEventType.UN_GROUP);
        C2c c2c = newC2c(params);
        return SendMessageTemplate.sendNotice(c2c);
    }

    @Override
    public boolean removeMemberFromGroup(long affairId, long groupId, long roleId) throws TException {
        ParamVo params = new ParamVo();
        params.setAffairId(affairId);
        params.setGroupId(groupId);
        params.setRoleId(roleId);
        params.setEventType(CacheUpdateEventType.REMOVE_MEMBER_FROM_GROUP);
        C2c c2c = newC2c(params);
        return SendMessageTemplate.sendNotice(c2c);
    }

    @Override
    public boolean addMemberToGroup(long affairId, long groupId, long roleId) throws TException {
        ParamVo params = new ParamVo();
        params.setAffairId(affairId);
        params.setGroupId(groupId);
        params.setRoleId(roleId);
        params.setEventType(CacheUpdateEventType.ADD_MEMBER_TO_GROUP);
        C2c c2c = newC2c(params);
        return SendMessageTemplate.sendNotice(c2c);
    }

    @Override
    public boolean removeMemberFromAffair(long affairId, long allianceId, long roleId, boolean isGuest) throws TException {
        ParamVo params = new ParamVo();
        params.setAffairId(affairId);
        params.setRoleId(roleId);
        params.setGuest(isGuest);
        params.setAllianceId(allianceId);
        params.setEventType(CacheUpdateEventType.REMOVE_MEMBER_FROM_AFFAIR);
        C2c c2c = newC2c(params);
        return SendMessageTemplate.sendNotice(c2c);
    }

    @Override
    public boolean addMemberToAffair(long affairId, long allianceId, long roleId, boolean isGuest) throws TException {
        ParamVo params = new ParamVo();
        params.setAffairId(affairId);
        params.setAllianceId(allianceId);
        params.setRoleId(roleId);
        params.setGuest(isGuest);
        params.setEventType(CacheUpdateEventType.ADD_MEMBER_TO_AFFAIR);
        C2c c2c = newC2c(params);
        return SendMessageTemplate.sendNotice(c2c);
    }

    @Override
    public boolean disableAffair(long affairId) throws TException {
        ParamVo params = new ParamVo();
        params.setAffairId(affairId);
        params.setEventType(CacheUpdateEventType.DISABLE_AFFAIR);
        C2c c2c = newC2c(params);
        return SendMessageTemplate.sendNotice(c2c);
    }

    private C2c newC2c(ParamVo params){
        C2c c2c = new C2c();
        c2c.setType(UPDATE_CACHE);
        c2c.setParams(new Gson().toJson(params));
        return c2c;
    }
}
