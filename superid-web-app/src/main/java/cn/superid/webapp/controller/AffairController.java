package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IUserService;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        createAffairForm.setAllianceId(GlobalValue.currentAllianceId());
        createAffairForm.setNumber(index);
        createAffairForm.setName(name);
        try {
           AffairEntity affairEntity= affairService.createAffair(createAffairForm);
            return SimpleResponse.ok(affairEntity.getId());
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }
    }


    @RequestMapping(value = "/get_direct_child_affair",method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getAllDirectChildAffair(){
        try{
            return SimpleResponse.ok(affairService.getAllDirectChildAffair(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId()));
        }catch (Exception e){
            return SimpleResponse.exception(e);
        }
    }

    @ApiOperation(value = "在失效或者移动等对事务的操作之前,检查该事务是否有特殊情况需要处理",response = String.class,notes = "0表示没毛病,1表示有子事务,2表示有交易")
    @RequestMapping(value = "/generate_affair",method = RequestMethod.POST)
    public SimpleResponse beforeGenerateAffair(long allianceId,long affairId){
        int condition ;
        try{
            condition = affairService.canGenerateAffair(allianceId,affairId);
            switch (condition){
                case 0:
                    return SimpleResponse.ok("没毛病");
                case 1:
                    return SimpleResponse.error("该事务拥有子事务");
                case 2:
                    return SimpleResponse.error("该事务下有正在进行的交易");
                default:
                    return SimpleResponse.error("大概还有我不知道的事");
            }

        }catch (Exception e){
            return SimpleResponse.exception(e);
        }
    }

    @ApiOperation(value = "失效一个事务",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/disable_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair=AffairPermissions.INVALID_AFFAIR)
    public SimpleResponse disableAffair(Long allianceId,Long affairId) {
        boolean success;
        try {
            success = affairService.disableAffair(allianceId,affairId);
            if(success) return SimpleResponse.ok("yep");
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }

        return SimpleResponse.error("因为某些奇怪的原因失败了");
    }


}
