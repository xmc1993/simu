package cn.superid.webapp.service.impl;

import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.util.Pagination;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
import cn.superid.utils.PingYinUtil;
import cn.superid.webapp.controller.VO.AffairUserInfoVO;
import cn.superid.webapp.controller.forms.SimpleRoleCard;
import cn.superid.webapp.dao.IAffairMemberDao;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.enums.state.DealState;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.enums.type.AffairMemberType;
import cn.superid.webapp.enums.type.InvitationType;
import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.GetRoleCardsMap;
import cn.superid.webapp.forms.SearchAffairMemberConditions;
import cn.superid.webapp.forms.SearchAffairRoleConditions;
import cn.superid.webapp.model.*;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.INoticeService;
import cn.superid.webapp.service.vo.AffairMemberSearchVo;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaofengxu on 16/9/2.
 */
@Service
public class AffairMemberService implements IAffairMemberService {

    @Autowired
    private IAffairUserService affairUserService;
    @Autowired
    private IAffairMemberDao affairMemberDao;
    @Autowired
    private IAffairService affairService;
    @Autowired
    private INoticeService noticeService;

    @Override
    public AffairMemberEntity addMember(Long allianceId, Long affairId, Long roleId, String permissions) {

        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setRoleId(roleId);
        affairMemberEntity.setState(ValidState.Valid);
        affairMemberEntity.setPermissions(permissions);
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.save();
        return affairMemberEntity;
    }


    @Override
    public AffairMemberEntity addCreator(long allianceId, long affairId, long userId, long roleId) {
        affairUserService.addAffairUser(allianceId, affairId, userId, roleId);
        return addMember(allianceId, affairId, roleId, AffairPermissionRoleType.OWNER);
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
    public int canApplyForEnterAffair(Long allianceId, Long affairId, Long roleId) {
        if (!affairService.isValidAffair(allianceId, affairId)) {
            return ResponseCode.AFFAIR_INVALID;
        }
        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id", affairId).eq("role_id", roleId).state(ValidState.Valid).exists();
        if (isExist) {
            return ResponseCode.MEMBER_IS_EXIST_IN_AFFAIR;
        }
        boolean isApplied = AffairMemberApplicationEntity.dao.partitionId(affairId).eq("role_id", roleId).state(DealState.ToCheck).exists();
        if (isApplied) {
            return ResponseCode.WAIT_FOR_DEAL;
        }
        return ResponseCode.OK;
    }

    @Override
    public int applyForEnterAffair(long userId, long allianceId, long affairId, long roleId, String applyReason) {
        int code = canApplyForEnterAffair(allianceId, affairId, roleId);
        if (code != 0) {
            return code;
        }
        AffairMemberApplicationEntity affairMemberApplicationEntity = new AffairMemberApplicationEntity();
        affairMemberApplicationEntity.setRoleId(roleId);
        affairMemberApplicationEntity.setUserId(userId);
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
    public int agreeAffairMemberApplication(long userId, long allianceId, long affairId, long applicationId, long dealRoleId, String dealReason) {

        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId, affairId);
        if ((affairMemberApplicationEntity == null) || (affairMemberApplicationEntity.getState() != DealState.ToCheck)) {
            return ResponseCode.APPLICATION_NOT_EXIST;
        }

        if (!affairService.isValidAffair(allianceId, affairId)) {
            return ResponseCode.AFFAIR_INVALID;
        }

        addMember(allianceId, affairId, affairMemberApplicationEntity.getRoleId(), "");
        affairUserService.addAffairUser(allianceId, affairId, affairMemberApplicationEntity.getUserId(), affairMemberApplicationEntity.getRoleId());

        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(DealState.Agree);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        affairMemberApplicationEntity.setDealUserId(userId);
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();

        return ResponseCode.OK;
    }

    @Override
    public int rejectAffairMemberApplication(long userId, long allianceId, long affairId, long applicationId, long dealRoleId, String dealReason) {
        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId, affairId);
        if (affairMemberApplicationEntity == null) {
            return ResponseCode.APPLICATION_NOT_EXIST;
        }
        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(DealState.Reject);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        affairMemberApplicationEntity.setDealUserId(userId);
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();
        return ResponseCode.OK;
    }


    private int canInviteToEnterAffair(Long allianceId, Long affairId, Long beInvitedRoleId) {
        //异常流程
        if (!affairService.isValidAffair(allianceId, affairId)) {
            return ResponseCode.AFFAIR_INVALID;
        }

        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affairId", affairId).eq("roleId", beInvitedRoleId).state(ValidState.Valid).exists();
        if (isExist) {
            return ResponseCode.MEMBER_IS_EXIST_IN_AFFAIR;
        }
        return ResponseCode.OK;
    }

    @Override
    public int inviteAllianceRoleToEnterAffair(long allianceId, long affairId, long inviteRoleId, long inviteUserId, List<Long> roles) {
        long beInvitedRoleId;
        Object[] roleIds = new Object[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            roleIds[i] = roles.get(i);
        }
        if ((roleIds == null || (roleIds.length == 0))) {
            return ResponseCode.OK;
        }
        List<RoleEntity> roleEntities = RoleEntity.dao.in("id", roleIds).selectList("id", "userId", "allianceId", "title");
        if ((null == roleEntities) || (roleEntities.size() == 0)) {
            return ResponseCode.Role_INVALID;
        }
        for (RoleEntity role : roleEntities) {
            if (role == null) {
                return ResponseCode.Role_INVALID;
            }
            //不能是盟外角色
            if (!(allianceId == role.getAllianceId())) {
                return ResponseCode.RoleNotInAlliance;
            }
            beInvitedRoleId = role.getId();
            int code = canInviteToEnterAffair(allianceId, affairId, beInvitedRoleId);
            if (code != 0) {
                return code;
            }

            InvitationEntity invitationEntity = new InvitationEntity();
            invitationEntity.setAllianceId(allianceId);
            invitationEntity.setAffairId(affairId);
            invitationEntity.setInviteUserId(inviteUserId);
            invitationEntity.setInviteRoleId(inviteRoleId);
            invitationEntity.setInviteReason("");
            invitationEntity.setBeInvitedUserId(role.getUserId());
            invitationEntity.setBeInvitedRoleId(role.getId());
            invitationEntity.setBeInvitedRoleTitle(role.getTitle());
            invitationEntity.setInvitationType(InvitationType.Affair);
            invitationEntity.setState(DealState.Agree);
            //盟内人员默认是参与者
            invitationEntity.setPermissions(AffairPermissionRoleType.PARTICIPANT);
            invitationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
            invitationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
            invitationEntity.save();
            //增加affairMember
            addMember(allianceId, affairId, beInvitedRoleId, AffairPermissionRoleType.PARTICIPANT);
            //增加affairUser
            affairUserService.addAffairUser(allianceId, affairId, invitationEntity.getBeInvitedUserId(), invitationEntity.getBeInvitedRoleId());
            noticeService.allianceInvitation(invitationEntity);
        }
        return ResponseCode.OK;
    }


    @Override
    public int inviteOutAllianceRoleToEnterAffair(long allianceId, long affairId, long inviteRoleId, long inviteUserId, List<Long> roles) {
        long beInvitedRoleId;
        Object[] roleIds = new Object[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            roleIds[i] = roles.get(i);
        }
        if ((roleIds == null || (roleIds.length == 0))) {
            return ResponseCode.OK;
        }
        List<RoleEntity> roleEntities = RoleEntity.dao.in("id", roleIds).selectList("id", "userId", "allianceId", "title");
        if ((null == roleEntities) || (roleEntities.size() == 0)) {
            return ResponseCode.Role_INVALID;
        }
        for (RoleEntity role : roleEntities) {
            if (role == null) {
                return ResponseCode.Role_INVALID;
            }
            //不能是盟内角色
            if (allianceId == role.getAllianceId()) {
                return ResponseCode.ROLE_IS_IN_ALLIANCE;
            }
            beInvitedRoleId = role.getId();
            int code = canInviteToEnterAffair(allianceId, affairId, beInvitedRoleId);
            if (code != 0) {
                return code;
            }
            InvitationEntity invitationEntity = InvitationEntity.dao.partitionId(allianceId).eq("beInvitedRoleId", beInvitedRoleId).eq("affairId", affairId).state(DealState.ToCheck).selectOne();
            //如果该角色已经被邀请过进入该事务,就更新邀请信息,继续发送通知
            if (invitationEntity != null) {
                invitationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
                invitationEntity.setInviteUserId(inviteUserId);
                invitationEntity.setInviteRoleId(inviteRoleId);
                invitationEntity.update();
            }
            //生成邀请信息,推送
            else {
                invitationEntity = new InvitationEntity();
                invitationEntity.setAllianceId(allianceId);
                invitationEntity.setAffairId(affairId);
                invitationEntity.setInviteUserId(inviteUserId);
                invitationEntity.setInviteRoleId(inviteRoleId);
                invitationEntity.setInviteReason("");
                invitationEntity.setBeInvitedUserId(role.getUserId());
                invitationEntity.setBeInvitedRoleId(role.getId());
                invitationEntity.setBeInvitedRoleTitle(role.getTitle());
                invitationEntity.setInvitationType(InvitationType.Affair);
                invitationEntity.setState(DealState.ToCheck);
                //盟外人员进来是盟客
                invitationEntity.setPermissions(AffairPermissionRoleType.MENKOR);
                invitationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
                invitationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
                invitationEntity.save();
            }
            noticeService.allianceInvitation(invitationEntity);
        }

        return ResponseCode.OK;
    }

    @Override
    public int agreeInvitation(long allianceId, long affairId, long invitationId, String dealReason) {
        InvitationEntity invitationEntity = InvitationEntity.dao.findById(invitationId, affairId);
        if ((invitationEntity == null) || (invitationEntity.getState() != DealState.ToCheck)) {
            return ResponseCode.INVITATION_INVALID;
        }
        if (affairService.isValidAffair(allianceId,affairId)) {
            return ResponseCode.AFFAIR_INVALID;
        }

        //加入事务
        //添加affairMember
        addMember(allianceId, affairId, invitationEntity.getBeInvitedRoleId(), invitationEntity.getPermissions());
        //添加affairUser
        affairUserService.addAffairUser(allianceId, affairId, invitationEntity.getBeInvitedUserId(), invitationEntity.getBeInvitedRoleId());

        //更新邀请信息
        invitationEntity.setDealReason(dealReason);
        invitationEntity.setState(DealState.Agree);
        invitationEntity.update();
        return ResponseCode.OK;
    }

    @Override
    public int rejectInvitation(long allianceId, long affairId, long invitationId, String dealReason) {
        InvitationEntity invitationEntity = InvitationEntity.dao.findById(invitationId, affairId);
        if ((invitationEntity == null) || (invitationEntity.getState() != DealState.ToCheck)) {
            return ResponseCode.INVITATION_INVALID;
        }

        //更新邀请信息
        invitationEntity.setDealReason(dealReason);
        invitationEntity.setState(DealState.Reject);
        invitationEntity.update();
        return ResponseCode.OK;

    }


    public boolean isOwnerOfParentAffair(long roleId, long affairId, long allianceId) {
        List<Long> directorIds = getDirectorIds(affairId, allianceId);
        for (long id : directorIds) {
            if (id == roleId) {
                return true;
            }
        }
        return false;

    }


    @Override
    public List<Long> getDirectorIds(long affairId, long allianceId) {
        AffairEntity affairEntity = AffairEntity.dao.id(affairId).partitionId(allianceId).selectOne("level", "path");
        int level = affairEntity.getLevel();
        String path = affairEntity.getPath();

        StringBuilder sb = new StringBuilder("");
        Object[] paths = new Object[level];
        String[] indexes = path.split("-");
        paths[0] = indexes[0];
        sb.append(indexes[0]);
        if (indexes.length > 1) {
            for (int i = 1; i < level; i++) {
                sb.append("-");
                sb.append(indexes[i]);
                paths[i] = sb.toString();
            }
        }
        return AffairEntity.dao.partitionId(allianceId).in("path", paths).selectList(Long.class, "owner_role_id");
    }


    @Override
    public List<AffairMemberEntity> getAffairGuestMembers(long allianceId, long affairId) {
        return AffairMemberEntity.dao.eq("affair_id", affairId).state(ValidState.Valid).neq("alliance_id", allianceId).selectList();
    }

    @Override
    public int countAffairMember(long allianceId, long affairId, Integer type) {
        ConditionalDao dao = AffairMemberEntity.dao.partitionId(allianceId).eq("affairId", affairId);
        return type == null ? dao.count() : dao.eq("type", type).count();
    }


    @Override
    public List<GetRoleCardsMap> searchAffairRoleCards(long allianceId, long affairId, SearchAffairRoleConditions conditions) {
        if (conditions.getLimit() < 10 || conditions.getLimit() > 100) conditions.setLimit(20);

        long[] affairIds;
        if (StringUtil.isEmpty(conditions.getAffairIds())) {
            affairIds = new long[1];
            affairIds[0] = affairId;
        } else {
            String[] ids = conditions.getAffairIds().split(",");
            affairIds = new long[ids.length];
            for (int i = 0; i < ids.length; i++) {//TODO 判断id是不是当前id的子事务,并且
                affairIds[i] = Long.parseLong(ids[i]);
            }
        }

        List<GetRoleCardsMap> list = new ArrayList<>(affairIds.length);
        for (long id : affairIds) {
            GetRoleCardsMap getRoleCardsMap = new GetRoleCardsMap();
            getRoleCardsMap.setAffairId(id);
            List<AffairRoleCard> affairRoleCards = affairMemberDao.searchAffairRoles(allianceId, affairId, conditions);
            getRoleCardsMap.setRoles(affairRoleCards);

            if (conditions.isNeedCount()) {
                int officialCount = 0, guestCount = 0;
                if (affairRoleCards.size() < conditions.getLimit()) {//已经全部取完,这时候完全可以得到各自的官方和客方数目
                    for (AffairRoleCard affairRoleCard : affairRoleCards) {
                        if (affairRoleCard.getType() == AffairMemberType.Official) {
                            officialCount++;
                        } else {
                            guestCount++;
                        }
                    }
                } else {
                    officialCount = this.countAffairMember(allianceId, affairId, AffairMemberType.Official);
                    guestCount = this.countAffairMember(allianceId, affairId, AffairMemberType.Guest);

                }
                getRoleCardsMap.setOfficialCount(officialCount);
                getRoleCardsMap.setGuestCount(guestCount);
            }
            list.add(getRoleCardsMap);
        }

        return list;
    }

    @Override
    public List<AffairMemberSearchVo> searchAffairMembers(long allianceId, long affairId, SearchAffairMemberConditions conditions, Pagination pagination) {
        return affairMemberDao.searchAffairMembers(allianceId, affairId, conditions, pagination);
    }

    //TODO  根据盟要求公开的信息来展示
    @Override
    public AffairUserInfoVO getAffairUserInfo(long allianceId, long userId) {
        UserEntity userEntity = UserEntity.dao.findById(userId);
        if (null == userEntity) {
            return null;
        }
        AffairUserInfoVO affairUserInfoVO = new AffairUserInfoVO();
        userEntity.copyPropertiesTo(affairUserInfoVO);

        String sql = "select r.id as roleId, r.title as roleTitle, r.belong_affair_id , a.name as belongAffairName from role r " +
                "join affair a on a.id = r.belong_affair_id " +
                "where r.alliance_id = ? and r.user_id = ?";
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(userId);
        List<SimpleRoleCard> simpleRoleCards = RoleEntity.getSession().findListByNativeSql(SimpleRoleCard.class, sql, p);
        affairUserInfoVO.setSimpleRoleCards(simpleRoleCards);

        return affairUserInfoVO;
    }


    private AffairRoleCard getRoleCard(long allianceId, long roleId, long affairId, boolean needMemberInfo) {
        AffairRoleCard affairRoleCard = new AffairRoleCard();
        if (needMemberInfo) {
            AffairMemberEntity affairMemberEntity = getAffairMemberInfo(allianceId, affairId, roleId);
            if (affairMemberEntity == null) {
                return null;
            }
            affairRoleCard.setPermissions(affairMemberEntity.getPermissions());
            affairRoleCard.setType(affairMemberEntity.getType());
        }
        RoleEntity roleEntity = RoleEntity.dao.findById(roleId, allianceId);
        affairRoleCard.setRoleId(roleEntity.getId());
        affairRoleCard.setRoleTitle(roleEntity.getTitle());
        affairRoleCard.setBelongAffairId(roleEntity.getBelongAffairId());

        AffairEntity belongAffair = AffairEntity.dao.partitionId(allianceId).id(roleEntity.getBelongAffairId()).selectOne("name");
        affairRoleCard.setBelongAffairName(belongAffair.getName());

        UserBaseInfo userBaseInfo = UserBaseInfo.dao.findById(roleEntity.getUserId());
        affairRoleCard.setAvatar(userBaseInfo.getAvatar());
        affairRoleCard.setGender(userBaseInfo.getGender());
        affairRoleCard.setNameAbbr(PingYinUtil.getFirstSpell(userBaseInfo.getUsername()));
        affairRoleCard.setUsername(userBaseInfo.getUsername());
        affairRoleCard.setUserId(userBaseInfo.getId());

        return affairRoleCard;
    }

    @Override
    public AffairRoleCard getRoleCard(long allianceId, long roleId, long affairId) {
        return getRoleCard(allianceId, roleId, affairId, true);
    }

    @Override
    public AffairRoleCard getDirectorCard(long allianceId, long affairId) {
        AffairEntity affairEntity = AffairEntity.dao.partitionId(allianceId).id(affairId).selectOne("ownerRoleId");
        if (affairEntity == null) {
            return null;
        }
        AffairRoleCard affairRoleCard = getRoleCard(allianceId, affairEntity.getOwnerRoleId(), affairId);
        if(affairRoleCard == null){
            return null;
        }
        affairRoleCard.setType(AffairMemberType.Official);
        affairRoleCard.setPermissions("*");
        return affairRoleCard;
    }
}
