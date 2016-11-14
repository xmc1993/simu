package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.forms.AffairInfo;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IUserService;
import com.alibaba.fastjson.JSON;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public SimpleResponse createAffair(String name , int publicType , Long affairMemberId , String logo , String description){
        CreateAffairForm createAffairForm = new CreateAffairForm();
        createAffairForm.setPublicType(publicType);
        createAffairForm.setOperationRoleId(GlobalValue.currentRoleId());
        createAffairForm.setAffairId(GlobalValue.currentAffairId());
        createAffairForm.setAllianceId(GlobalValue.currentAllianceId());
        createAffairForm.setName(name);
        createAffairForm.setLogo(logo);
        createAffairForm.setDescription(description);
        try {
           AffairEntity affairEntity= affairService.createAffair(createAffairForm);
            return SimpleResponse.ok(affairEntity.getId());
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }
    }


    @RequestMapping(value = "/get_direct_child_affair",method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getAllDirectChildAffair(Long affairMemberId){
        try{
            return SimpleResponse.ok(affairService.getAllDirectChildAffair(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId()));
        }catch (Exception e){
            return SimpleResponse.exception(e);
        }
    }

    @ApiOperation(value = "在失效或者移动等对事务的操作确认之前,比如点击移动按钮,检查该事务是否有特殊情况需要处理",response = String.class,notes = "0表示没毛病,1表示有子事务,2表示有交易")
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

    @ApiOperation(value = "移动一个事务,确认移动的时候调用",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/move_affair",method = RequestMethod.POST)
    public SimpleResponse moveAffair(){
        return null;
    }


    @ApiOperation(value = "失效一个事务,确认失效的时候调用",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/disable_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair=AffairPermissions.INVALID_AFFAIR)
    public SimpleResponse disableAffair(long affairMemberId) {
        boolean success;
        try {
            success = affairService.disableAffair(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId());
            if(success) return SimpleResponse.ok("yep");
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }

        return SimpleResponse.error("因为某些奇怪的原因失败了");
    }

    @ApiOperation(value = "添加封面",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/add_covers", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse addCovers(String urls , Long affairMemberId ) {
        if(urls == null){
            return SimpleResponse.error("url不能为空");
        }
        boolean result = affairService.addCovers(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId(),urls);
        if(result == false){
            return SimpleResponse.error("添加失败");
        }
        return SimpleResponse.ok(affairService.getCovers(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId()));
    }

    @ApiOperation(value = "设置默认封面",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/set_default_cover", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse setDefaultCover(Long coverId , Long affairMemberId ) {
        if(coverId == null){
            return SimpleResponse.error("coverId不能为空");
        }
        boolean result = affairService.setDefaultCover(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId(),coverId);

        if(result == false){
            return SimpleResponse.error("设置失败");
        }
        return SimpleResponse.ok(affairService.getCovers(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId()));

    }

    @ApiOperation(value = "查看封面",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/get_covers", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getCovers(Long affairMemberId) {
        return SimpleResponse.ok(affairService.getCovers(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId()));
    }

    @ApiOperation(value = "事务概览",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/overview_affair", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse overviewAffair(Long affairMemberId) {
        Map<String, Object> rsMap  = affairService.affairOverview(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId());
        return SimpleResponse.ok(rsMap);
    }

    @ApiOperation(value = "查看所有人员",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/get_member", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getMember(Long affairMemberId) {
        return SimpleResponse.ok(affairService.getAllRoles(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId()));
    }


    @ApiOperation(value = "获取事务首页必要的信息",response = String.class,notes = "publicType事务公开性:0完全公开 1盟内可见 2成员可见")
    @RequestMapping(value = "/affair_info",method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.AffairInfo)
    public SimpleResponse getAffairInfo(long affairMemberId){
        long allianceId = GlobalValue.currentAllianceId();
        long affairId = GlobalValue.currentAffairId();
        AffairInfo affairInfo = affairService.getAffairInfo(allianceId,affairId);
        return SimpleResponse.ok(affairInfo);

    }

}
