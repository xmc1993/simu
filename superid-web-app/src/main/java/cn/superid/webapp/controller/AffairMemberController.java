package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairMemberService;
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

    @ApiOperation(value = "同意进入事务申请",response = AffairMemberEntity.class, notes = "拥有同意申请的权限")
    @RequestMapping(value = "/agree_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.ADD_AFFAIR_MEMBER,AffairPermissions.GENERATE_PERMISSION_GROUP})
    public SimpleResponse agreeAffairMemberApplication(long affairMemberId,long applicationId,String dealReason) {
        int code = affairMemberService.agreeAffairMemberApplication(GlobalValue.currentAllianceId(),
                GlobalValue.currentAffairId(),applicationId, GlobalValue.currentRoleId(),dealReason);
        switch (code){
            case ResponseCode.OK:
                return SimpleResponse.ok("you agree this application");
            case ResponseCode.AffairNotExist:
                return new SimpleResponse(ResponseCode.AffairNotExist,"this affair is not exist");
            case ResponseCode.ApplicationNotExist:
                return new SimpleResponse(ResponseCode.ApplicationNotExist,"this application is not exist");
            default:
                return SimpleResponse.error("fail");
        }

    }

    @ApiOperation(value = "拒绝进入事务申请",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/reject_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse disagreeAffairMemberApplication(long affairMemberId,long applicationId,String dealReason) {
        int code = affairMemberService.rejectAffairMemberApplication(GlobalValue.currentAllianceId(),
                GlobalValue.currentAffairId(),applicationId, GlobalValue.currentRoleId(),dealReason);
        switch (code){
            case ResponseCode.OK:
                return SimpleResponse.ok("you reject this application");
            case ResponseCode.AffairNotExist:
                return new SimpleResponse(ResponseCode.AffairNotExist,"this affair is not exist");
            case ResponseCode.ApplicationNotExist:
                return new SimpleResponse(ResponseCode.ApplicationNotExist,"this application is not exist");
            default:
                return SimpleResponse.error("fail");
        }
    }

    @ApiOperation(value = "申请加入事务",response = String.class, notes = "")
    @RequestMapping(value = "/apply_for_enter_affair",method = RequestMethod.POST)
    public SimpleResponse applyForEnterAffair(long allianceId,long affairId,long affairMemberId,String applyReason){
        int code = affairMemberService.applyForEnterAffair(allianceId,affairId,GlobalValue.currentRoleId(),applyReason);
        switch (code){
            case ResponseCode.OK:
                return SimpleResponse.ok("success! please wait for deal");
            case ResponseCode.AffairNotExist:
                return new SimpleResponse(ResponseCode.AffairNotExist,"this affair is not exist");
            case ResponseCode.MemberIsExistInAffair:
                return new SimpleResponse(ResponseCode.MemberIsExistInAffair,"you are in this affair");
            case ResponseCode.WaitForDeal:
                return new SimpleResponse(ResponseCode.WaitForDeal,"you have applied before,please wait for deal");
            default:
                return new SimpleResponse(ResponseCode.Error,"apply fail");
        }
    }

    @RequestMapping(value = "/invite_to_enter_affair",method = RequestMethod.POST)
    public SimpleResponse inviteToEnterAffair(long affairMemberId,long beInvitedRoleId,String inviteReason){
        int code = affairMemberService.inviteToEnterAffair(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId(),
                GlobalValue.currentRoleId(),GlobalValue.currentRole().getUserId(),beInvitedRoleId,inviteReason);
        return SimpleResponse.ok("");
    }
}
