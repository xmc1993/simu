package cn.superid.webapp.controller;

import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.IUserService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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

    @ApiOperation(value = "同意进入事务申请", response = AffairMemberEntity.class, notes = "拥有同意申请的权限")
    @RequestMapping(value = "/agree_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.ADD_AFFAIR_ROLE})
    public SimpleResponse agreeAffairMemberApplication(long affairMemberId, long applicationId, String dealReason) {
        int code = affairMemberService.agreeAffairMemberApplication(GlobalValue.currentAllianceId(),
                GlobalValue.currentAffairId(), applicationId, GlobalValue.currentRoleId(), dealReason);
        return new SimpleResponse(code, null);

    }

    @ApiOperation(value = "拒绝进入事务申请", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/reject_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.ADD_AFFAIR_ROLE})
    public SimpleResponse disagreeAffairMemberApplication(long affairMemberId, long applicationId, String dealReason) {
        int code = affairMemberService.rejectAffairMemberApplication(GlobalValue.currentAllianceId(),
                GlobalValue.currentAffairId(), applicationId, GlobalValue.currentRoleId(), dealReason);
        return new SimpleResponse(code, null);

    }

    @ApiOperation(value = "申请加入事务", response = String.class, notes = "")
    @RequestMapping(value = "/apply_for_enter_affair", method = RequestMethod.POST)
    public SimpleResponse applyForEnterAffair(long roleId, long allianceId, long targetAllianceId, long targetAffairId, String applyReason) {
        int code = affairMemberService.canApplyForEnterAffair(targetAllianceId,targetAffairId,roleId);
        if(code != 0){
            return new SimpleResponse(code,null);
        }
        if(allianceId == targetAllianceId){
            boolean isOwner = affairMemberService.isOwnerOfParentAffair(roleId,targetAffairId,targetAllianceId);
            if(isOwner){
                affairMemberService.addMember(targetAllianceId, targetAffairId, roleId, AffairPermissionRoleType.OWNER);
                return SimpleResponse.ok(null);
            }
        }
        code = affairMemberService.applyForEnterAffair(targetAllianceId, targetAffairId, roleId, applyReason);
        return new SimpleResponse(code,null);
    }

    @RequiredPermissions(affair = AffairPermissions.ADD_AFFAIR_ROLE)
    @RequestMapping(value = "/invite_to_enter_affair", method = RequestMethod.POST)
    public SimpleResponse inviteToEnterAffair(long affairMemberId, long beInvitedRoleId, int memberType, String inviteReason) {
        int code = affairMemberService.canInviteToEnterAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(), beInvitedRoleId);
        if (code != 0) {
            return new SimpleResponse(code, null);
        }
        code = affairMemberService.inviteToEnterAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(),
                GlobalValue.currentRoleId(), GlobalValue.currentRole().getUserId(), beInvitedRoleId, memberType, inviteReason);
        return new SimpleResponse(code, null);

    }


    @ApiOperation(value = "获取某个事务的所有直系负责人", response = AffairMemberEntity.class, notes = "当要修改某个角色权限时,需要判断在不在这个负责人系列里面")
    @RequestMapping(value = "/get_directors", method = RequestMethod.GET)
    public SimpleResponse getDirectors(long affairMemberId) {
        List<Long> ids = affairMemberService.getDirectorIds(GlobalValue.currentAffairId(), GlobalValue.currentAllianceId());
        return SimpleResponse.ok(StringUtil.join(ids, ","));
    }

}
