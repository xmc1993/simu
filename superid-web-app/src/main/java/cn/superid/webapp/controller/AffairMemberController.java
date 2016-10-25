package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.security.AffairPermissions;
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
@RequestMapping(value = "affair_member")
public class AffairMemberController {
    @Autowired
    private IAffairMemberService affairMemberService;

    public SimpleResponse addMember(Long allianceId,Long affairId, Long roleId,  String permissions,long permissionGroupId){
        try {
            return SimpleResponse.ok(affairMemberService.addMember(allianceId,affairId,roleId,permissions,permissionGroupId));
        }catch (Exception e){
            return SimpleResponse.error("添加成员失败");
        }

    }


    @ApiOperation(value = "同意进入事务申请",response = AffairMemberEntity.class, notes = "拥有同意申请的权限")
    @RequestMapping(value = "/agree_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.ADD_AFFAIR_MEMBER,AffairPermissions.GENERATE_PERMISSION_GROUP})
    public SimpleResponse agreeAffairMemberApplication(Long allianceId,Long affairId,Long applicationId,Long dealRoleId,String dealReason) {
        try {
            AffairMemberApplicationEntity applicationEntity = affairMemberService.findAffairMemberApplicationById(affairId,applicationId);
            if(applicationEntity == null) {
                return new SimpleResponse(100, "Can't find this application");
            }
            AffairMemberEntity affairMemberEntity = affairMemberService.agreeAffairMemberApplication(allianceId,affairId,applicationId, dealRoleId,dealReason);
            return new SimpleResponse(ResponseCode.OK, affairMemberEntity);
        } catch (Exception e) {
            // LOG.error("agree affair member application error", e);
            return new SimpleResponse(100, e.getMessage());
        }
    }

    @ApiOperation(value = "拒绝进入事务申请",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/reject_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse disagreeAffairMemberApplication(Long allianceId,Long affairId, Long applicationId,Long dealRoleId,String dealReason) {
        try {
            affairMemberService.rejectAffairMemberApplication(allianceId,affairId,applicationId, dealRoleId,dealReason);
            return new SimpleResponse(ResponseCode.OK, "已拒绝此申请");
        } catch (Exception e) {
            //LOG.error("disagree affair member application error", e);
            return new SimpleResponse(100, e.getMessage());
        }
    }
}
