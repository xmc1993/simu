package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.controller.VO.SimpleRoleVO;
import cn.superid.webapp.dao.impl.IAffairMemberDao;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.enums.state.DealState;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.SearchAffairRoleConditions;
import cn.superid.webapp.model.*;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.vo.AffairMemberVO;
import cn.superid.webapp.utils.TimeUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaofengxu on 16/9/2.
 */
@Service
public class AffairMemberService implements IAffairMemberService {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAffairUserService affairUserService;
    @Autowired
    private IAffairMemberDao affairMemberDao;

    @Override
    public AffairMemberEntity addMember(Long allianceId, Long affairId, Long roleId, String permissions, int permissionLevel) {

        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setRoleId(roleId);
        affairMemberEntity.setState(ValidState.Valid);
        affairMemberEntity.setPermissions(permissions);
        affairMemberEntity.setPermissionLevel(permissionLevel);
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.save();

        affairUserService.addAffairUser(allianceId,affairId,roleId);

        return affairMemberEntity;
    }


    @Override
    public AffairMemberEntity addCreator(long allianceId, long affairId, long roleId) {
        return addMember(allianceId, affairId, roleId, AffairPermissionRoleType.OWNER, AffairPermissionRoleType.OWNER_ID);
    }

    @Override
    public AffairMemberEntity getAffairMemberInfo(long allianceId, long affairId, long roleId) {
        return AffairMemberEntity.dao.partitionId(allianceId).eq("affairId", affairId).eq("roleId", roleId).selectOne();
    }


    @Override
    public boolean modifyAffairMemberPermissions(Long allianceId, Long affairId, Long toRoleId, String permissions) throws Exception {

        if (StringUtil.isEmpty(permissions)) {
            throw new Exception("请选择正确的权限");
        }

        int updateCount = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id", affairId).eq("role_id", toRoleId).set("permissions", permissions);
        return updateCount > 0;
    }

    @Override
    public PermissionGroupEntity addPermissionGroup(Long allianceId, Long affairId, String name, String permissions) throws Exception {
        boolean isExist = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if (!isExist) {
            throw new Exception("找不到该事务");
        }
        if (StringUtil.isEmpty(permissions)) {
            throw new Exception("请选择正确的权限");
        }
        PermissionGroupEntity permissionGroupEntity = new PermissionGroupEntity();
        permissionGroupEntity.setName(name);
        permissionGroupEntity.setAffairId(affairId);
        permissionGroupEntity.setPermissions(permissions);
        permissionGroupEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        permissionGroupEntity.save();
        return permissionGroupEntity;
    }

    @Override
    public int canApplyForEnterAffair(Long allianceId, Long affairId, Long roleId) {
        boolean affairIsFind = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if (!affairIsFind) {
            return ResponseCode.AffairNotExist;
        }
        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id", affairId).eq("role_id", roleId).state(ValidState.Valid).exists();
        if (isExist) {
            return ResponseCode.MemberIsExistInAffair;
        }
        boolean isApplied = AffairMemberApplicationEntity.dao.partitionId(affairId).eq("role_id", roleId).state(DealState.ToCheck).exists();
        if (isApplied) {
            return ResponseCode.WaitForDeal;
        }
        return ResponseCode.OK;
    }

    @Override
    public int applyForEnterAffair(Long allianceId, Long affairId, Long roleId, String applyReason) {
        int code = canApplyForEnterAffair(allianceId, affairId, roleId);
        if (code != 0) {
            return code;
        }

        AffairMemberApplicationEntity affairMemberApplicationEntity = new AffairMemberApplicationEntity();
        affairMemberApplicationEntity.setRoleId(roleId);
        affairMemberApplicationEntity.setUserId(userService.currentUserId());
        affairMemberApplicationEntity.setAffairId(affairId);
        affairMemberApplicationEntity.setAllianceId(allianceId);
        affairMemberApplicationEntity.setState(DealState.ToCheck);
        affairMemberApplicationEntity.setApplyReason(applyReason);
        affairMemberApplicationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setDealReason("");
        affairMemberApplicationEntity.save();
        return ResponseCode.OK;
    }


    @Override
    public int agreeAffairMemberApplication(Long allianceId, Long affairId, Long applicationId, Long dealRoleId, String dealReason) {

        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId, affairId);
        if ((affairMemberApplicationEntity == null) || (affairMemberApplicationEntity.getState() != DealState.ToCheck)) {
            return ResponseCode.ApplicationNotExist;
        }

        boolean isExist = AffairEntity.dao.id(affairMemberApplicationEntity.getAffairId()).partitionId(allianceId).exists();
        if (!isExist) {
            return ResponseCode.AffairNotExist;
        }

        addMember(allianceId, affairId, affairMemberApplicationEntity.getRoleId(), "", AffairPermissionRoleType.GUEST_ID);

        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(DealState.Agree);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        affairMemberApplicationEntity.setDealUserId(userService.currentUserId());
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();

        return ResponseCode.OK;
    }

    @Override
    public int rejectAffairMemberApplication(Long allianceId, Long affairId, Long applicationId, Long dealRoleId, String dealReason) {
        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId, affairId);
        if (affairMemberApplicationEntity == null) {
            return ResponseCode.ApplicationNotExist;
        }
        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(DealState.Reject);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        affairMemberApplicationEntity.setDealUserId(userService.currentUserId());
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();
        return ResponseCode.OK;
    }

    @Override
    public int canInviteToEnterAffair(Long allianceId, Long affairId, Long beInvitedRoleId) {
        //异常流程
        boolean affairIsFind = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if (!affairIsFind) {
            return ResponseCode.AffairNotExist;
        }
        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affairId", affairId).eq("roleId", beInvitedRoleId).state(ValidState.Valid).exists();
        if (isExist) {
            return ResponseCode.MemberIsExistInAffair;
        }
        boolean isInvited = AffairMemberInvitationEntity.dao.partitionId(affairId).eq("beInvitedRoleId", beInvitedRoleId).state(DealState.ToCheck).exists();
        if (isInvited) {
            return ResponseCode.WaitForDeal;
        }
        return ResponseCode.OK;
    }

    @Override
    public int inviteToEnterAffair(long allianceId, long affairId, long inviteRoleId, long inviteUserId, long beInvitedRoleId, int memberType, String inviteReason) {
        int code = canInviteToEnterAffair(allianceId, affairId, beInvitedRoleId);
        if (code != 0) {
            return code;
        }
        //生成邀请记录
        AffairMemberInvitationEntity affairMemberInvitationEntity = new AffairMemberInvitationEntity();
        affairMemberInvitationEntity.setState(DealState.ToCheck);
        affairMemberInvitationEntity.setAffairId(affairId);
        affairMemberInvitationEntity.setBeInvitedRoleId(beInvitedRoleId);
        affairMemberInvitationEntity.setBeInvitedUserId(RoleCache.dao.findById(beInvitedRoleId).getUserId());
        affairMemberInvitationEntity.setInviteRoleId(inviteRoleId);
        affairMemberInvitationEntity.setInviteUserId(inviteUserId);
        affairMemberInvitationEntity.setInviteReason(inviteReason);
        affairMemberInvitationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        if (memberType == 0) {
            affairMemberInvitationEntity.setPermissionLevel(AffairPermissionRoleType.OFFICIAL_ID);
        } else {
            affairMemberInvitationEntity.setPermissionLevel(AffairPermissionRoleType.GUEST_ID);
        }
        affairMemberInvitationEntity.save();

        //判断被邀请的是否是本盟成员,如果是则无需同意,直接拉入事务
        boolean isInSameAlliance = RoleEntity.dao.id(beInvitedRoleId).partitionId(allianceId).exists();
        if (isInSameAlliance) {
            AffairMemberInvitationEntity.dao.id(affairMemberInvitationEntity.getId()).partitionId(affairId)
                    .set("state", DealState.Agree, "dealReason", "本盟人员");

            AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
            affairMemberEntity.setAffairId(affairId);
            affairMemberEntity.setAllianceId(allianceId);
            affairMemberEntity.setRoleId(beInvitedRoleId);
            affairMemberEntity.setState(ValidState.Valid);
            //判断被邀请的角色是不是自己的某个父事务的负责人
            AffairEntity currentAffair = AffairEntity.dao.id(affairId).partitionId(allianceId).selectOne("id");
            if (isOwnerOfParentAffair(beInvitedRoleId, currentAffair.getId(), allianceId)) {
                //如果是,将权限设置为owner
                affairMemberEntity.setPermissionLevel(AffairPermissionRoleType.OWNER_ID);
                AffairMemberInvitationEntity.dao.id(affairMemberInvitationEntity.getId()).partitionId(affairId)
                        .set("permissionLevel", AffairPermissionRoleType.OWNER_ID);
            } else {
                //如果不是,根据前端选择的权限类型分配给其官方还是客方
                if (memberType == 0) {
                    affairMemberEntity.setPermissionLevel(AffairPermissionRoleType.OFFICIAL_ID);
                } else {
                    affairMemberEntity.setPermissionLevel(AffairPermissionRoleType.GUEST_ID);
                }
            }
            affairMemberEntity.save();

            //TODO 发送消息通知
            //affairMemberEntity.setPermissions(AffairPermissionRoleType.);
        }
        //不是本盟成员
        else {
            //TODO 发送消息通知
        }
        return ResponseCode.OK;
    }

    @Override
    public int agreeInvitation(long allianceId, long affairId, long invitationId, String dealReason) {
        AffairMemberInvitationEntity affairMemberInvitationEntity = AffairMemberInvitationEntity.dao.findById(invitationId, affairId);
        if ((affairMemberInvitationEntity == null) || (affairMemberInvitationEntity.getState() != DealState.ToCheck)) {
            return ResponseCode.InvitationNotExist;
        }
        boolean isExist = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if (!isExist) {
            return ResponseCode.AffairNotExist;
        }

        //加入事务
        addMember(allianceId, affairId, affairMemberInvitationEntity.getBeInvitedRoleId(), "", affairMemberInvitationEntity.getPermissionLevel());
        //更新邀请信息
        affairMemberInvitationEntity.setDealReason(dealReason);
        affairMemberInvitationEntity.setState(DealState.Agree);
        affairMemberInvitationEntity.update();
        return ResponseCode.OK;
    }

    @Override
    public int rejectInvitation(long allianceId, long affairId, long invitationId, String dealReason) {
        AffairMemberInvitationEntity affairMemberInvitationEntity = AffairMemberInvitationEntity.dao.findById(invitationId, affairId);
        if ((affairMemberInvitationEntity == null) || (affairMemberInvitationEntity.getState() != DealState.ToCheck)) {
            return ResponseCode.InvitationNotExist;
        }

        //更新邀请信息
        affairMemberInvitationEntity.setDealReason(dealReason);
        affairMemberInvitationEntity.setState(DealState.Reject);
        affairMemberInvitationEntity.update();
        return ResponseCode.OK;

    }


    public boolean isOwnerOfParentAffair(long roleId, long affairId,long allianceId) {
        List<Long> directorIds = getDirectorIds(affairId,allianceId);
        for(long id:directorIds){
            if(id==roleId){
                return true;
            }
        }
        return false;

    }


    @Override
    public List<Long> getDirectorIds(long affairId, long allianceId) {
        AffairEntity affairEntity = AffairEntity.dao.id(affairId).partitionId(allianceId).selectOne("level","path");
        int level = affairEntity.getLevel();
        String path = affairEntity.getPath();

        StringBuilder sb = new StringBuilder("");
        Object[] paths = new Object[level];
        String[] indexes = path.split("-");
        paths[0] = indexes[0];
        sb.append(indexes[0]);
        if(indexes.length>1){
            for(int i=1;i<level;i++){
                sb.append("-");
                sb.append(indexes[i]);
                paths[i] = sb.toString();
            }
        }
        return (List<Long>) AffairEntity.dao.partitionId(allianceId).in("path",paths).selectList(Long.class,"owner_role_id");
    }


    @Override
    public List<AffairMemberEntity> getAffairGuestMembers(long allianceId, long affairId) {
        return AffairMemberEntity.dao.eq("affair_id", affairId).state(ValidState.Valid).neq("alliance_id", allianceId).selectList();
    }

    @Override
    public int countAffairMember(long allianceId, long affairId) {
        return AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id", affairId).count();
    }

    @Override
    public Map<Long, List<Object>> getAffairMember() {
        StringBuilder sb = new StringBuilder("select a.* , b.title from affair_member a join (select id,user_id,title from role where user_id = ? ) b on a.role_id = b.id ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(userService.currentUserId());
        List<AffairMemberVO> affairMemberVOList = AffairMemberEntity.getSession().findListByNativeSql(AffairMemberVO.class,sb.toString(),p);
        return getMaps(affairMemberVOList);
    }

    @Override
    public Map<Long, List<Object>> getAffairMemberByAllianceId(long allianceId) {
        StringBuilder sb = new StringBuilder("select a.* , b.title from affair_member a join (select id,user_id,title from role where alliance_id = ? and user_id = ? ) b on a.role_id = b.id ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(userService.currentUserId());
        List<AffairMemberVO> affairMemberVOList = AffairMemberEntity.getSession().findListByNativeSql(AffairMemberVO.class,sb.toString(),p);

        return getMaps(affairMemberVOList);
    }

    private Map<Long, List<Object>> getMaps(List<AffairMemberVO> affairMemberVOList){
        Map<Long,List<Object>> members = new HashedMap();
        for(AffairMemberVO a : affairMemberVOList){
            List<Object> user = new ArrayList<>();
            user.add(a.getAffairId());
            SimpleRoleVO role = new SimpleRoleVO(a.getRoleId(),a.getTitle());
            user.add(role);
            members.put(a.getId(),user);
        }
        return members;
    }

    @Override
    public List<AffairRoleCard> searchAffairRoleCards(long allianceId, long affairId, SearchAffairRoleConditions conditions) {
//        long[] affairIds;
//        if(StringUtil.isEmpty(conditions.getAffairIds())){
//            affairIds = new long[1];
//            affairIds[1] =affairId;
//        }else{
//            String[] ids =conditions.getAffairIds().split(",");
//            affairIds = new long[ids.length];
//            for(int i=0;i<ids.length;i++){//TODO 判断id是不是当前id的子事务,并且
//                affairIds[i] = Long.parseLong(ids[i]);
//            }
//        }

        return affairMemberDao.searchAffairRoles(allianceId,affairId,conditions);
    }
}
