package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IUserService;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by xiaofengxu on 16/8/24.
 */
@Controller
@RequestMapping("/affair")
public class AffairController {
    @Autowired
    private IAffairService affairService;
    @Autowired
    private IUserService userService;
    /**
     * 增加事务,参数
     */
    @ApiOperation(value = "添加事务", response = boolean.class, notes = "凡是事务内所有操作都需要affairMemberId;返回新建的事务id")
    @RequiredPermissions(affair = AffairPermissions.CREATE_AFFAIR)
    @RequestMapping(value = "/create_affair", method = RequestMethod.POST)
    public SimpleResponse createAffair(String name,int index,int publicType){
        CreateAffairForm createAffairForm = new CreateAffairForm();
        createAffairForm.setPublicType(publicType);
        createAffairForm.setOperationRoleId(GlobalValue.currentRoleId());
        createAffairForm.setAffairId(GlobalValue.currentAffairId());
        createAffairForm.setNumber(index);
        createAffairForm.setName(name);
        try {
           AffairEntity affairEntity= affairService.createAffair(createAffairForm);
            return SimpleResponse.ok(affairEntity.getId());
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }
    }

    /**
     *申请的id放在url传过来,再将处理人的角色id传过来
     */
    @ApiOperation(value = "同意进入事务申请",response = AffairMemberEntity.class, notes = "拥有同意申请的权限")
    @RequestMapping(value = "/agree_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.ADD_AFFAIR_MEMBER,AffairPermissions.GENERATE_PERMISSION_GROUP})
    public SimpleResponse agreeAffairMemberApplication(Long allianceId,Long affairId,Long applicationId,Long dealRoleId,String dealReason) {
        try {
            AffairMemberApplicationEntity applicationEntity = affairService.findAffairMemberApplicationById(affairId,applicationId);
            if(applicationEntity == null) {
                return new SimpleResponse(100, "Can't find this application");
            }
            AffairMemberEntity affairMemberEntity = affairService.agreeAffairMemberApplication(allianceId,affairId,applicationId, dealRoleId,dealReason);
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
            affairService.rejectAffairMemberApplication(allianceId,affairId,applicationId, dealRoleId,dealReason);
            return new SimpleResponse(ResponseCode.OK, "已拒绝此申请");
        } catch (Exception e) {
            //LOG.error("disagree affair member application error", e);
            return new SimpleResponse(100, e.getMessage());
        }
    }

    @RequestMapping(value = "/disable_affair", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse disableAffair(Long allianceId,Long affairId) {
        boolean success;
        try {
            success = affairService.disableAffair(allianceId,affairId);
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }
        if (success) {
            return SimpleResponse.ok("yep");
        }
        return SimpleResponse.error("因为某些奇怪的原因失败了");
    }


}
