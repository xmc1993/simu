package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.IUserService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by njuTms on 16/9/14.
 */
@Controller
@RequestMapping(value = "/affair_member")
public class AffairMemberController {
    @Autowired
    private IAffairMemberService affairMemberService;
    @Autowired
    private IAffairUserService affairUserService;
    @Autowired
    private IUserService userService;
    /*
    @ApiOperation(value = "添加事务成员",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/add_member",method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ADD_AFFAIR_MEMBER)
    public SimpleResponse addMember(Long allianceId,Long affairId, Long roleId,  String permissions,long permissionGroupId){
        try {
            return SimpleResponse.ok(affairMemberService.addMember(allianceId,affairId,roleId,permissions,permissionGroupId));
        }catch (Exception e){
            return SimpleResponse.error("添加成员失败");
        }

    }
*/

    @ApiOperation(value = "同意进入事务申请", response = AffairMemberEntity.class, notes = "拥有同意申请的权限")
    @RequestMapping(value = "/agree_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.ADD_AFFAIR_MEMBER})
    public SimpleResponse agreeAffairMemberApplication(long affairMemberId, long applicationId, String dealReason) {
        int code = affairMemberService.agreeAffairMemberApplication(GlobalValue.currentAllianceId(),
                GlobalValue.currentAffairId(), applicationId, GlobalValue.currentRoleId(), dealReason);
        return new SimpleResponse(code,null);

    }

    @ApiOperation(value = "拒绝进入事务申请", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/reject_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.ADD_AFFAIR_MEMBER})
    public SimpleResponse disagreeAffairMemberApplication(long affairMemberId, long applicationId, String dealReason) {
        int code = affairMemberService.rejectAffairMemberApplication(GlobalValue.currentAllianceId(),
                GlobalValue.currentAffairId(), applicationId, GlobalValue.currentRoleId(), dealReason);
        return new SimpleResponse(code,null);

    }

    @ApiOperation(value = "申请加入事务", response = String.class, notes = "")
    @RequestMapping(value = "/apply_for_enter_affair", method = RequestMethod.POST)
    public SimpleResponse applyForEnterAffair(long roleId,long allianceId,long targetAllianceId, long targetAffairId, String applyReason) {
        int code = affairMemberService.canApplyForEnterAffair(targetAllianceId,targetAffairId,roleId);
        if(code != 0){
            return new SimpleResponse(code,null);
        }
        if(allianceId == targetAllianceId){
            boolean isOwner = affairMemberService.isOwnerOfParentAffair(roleId,targetAffairId,targetAllianceId);
            if(isOwner){
                affairMemberService.addMember(targetAllianceId, targetAffairId, roleId, AffairPermissionRoleType.OWNER, AffairPermissionRoleType.OWNER_ID);
                affairUserService.addAffairUser(targetAllianceId,targetAffairId,roleId,userService.currentUserId());
                return SimpleResponse.ok(null);
            }
        }


        code = affairMemberService.applyForEnterAffair(targetAllianceId, targetAffairId, roleId, applyReason);
        return new SimpleResponse(code,null);
    }

    @RequiredPermissions(affair = AffairPermissions.ADD_AFFAIR_MEMBER)
    @RequestMapping(value = "/invite_to_enter_affair", method = RequestMethod.POST)
    public SimpleResponse inviteToEnterAffair(long affairMemberId, long beInvitedRoleId, int memberType, String inviteReason) {
        int code = affairMemberService.canInviteToEnterAffair(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId(),beInvitedRoleId);
        if(code!=0){
            return new SimpleResponse(code,null);
        }
        code = affairMemberService.inviteToEnterAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(),
                GlobalValue.currentRoleId(), GlobalValue.currentRole().getUserId(), beInvitedRoleId, memberType, inviteReason);
        return new SimpleResponse(code,null);

    }
}
