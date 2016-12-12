package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.forms.AffairInfo;
import cn.superid.webapp.enums.state.AffairMoveState;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.forms.ModifyAffairInfoForm;
import com.sun.istack.internal.NotNull;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
            Map<String,Object> result= affairService.createAffair(createAffairForm);
            return SimpleResponse.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
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

    //TODO 标签待定
    @ApiOperation(value = "修改事务信息,将修改的字段传过来即可,affairMemberId必需",response = String.class,notes = "form里包含需要修改的信息,没有修改的字段可以不传,字段名和返回的affairInfo字段名一致")
    @RequestMapping(value = "/modify_affair_info",method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.EDIT_AFFAIR_INFO)
    public SimpleResponse modifyAffairInfo(long affairMemberId,@RequestBody ModifyAffairInfoForm modifyAffairInfoForm){
        boolean isModified = affairService.modifyAffairInfo(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId(),modifyAffairInfoForm);
        if(isModified){
            return SimpleResponse.ok("edit success");
        }
        else {
            return SimpleResponse.error("fail");
        }

    }

    @ApiOperation(value = "失效一个事务,确认失效的时候调用",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/disable_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair=AffairPermissions.INVALID_AFFAIR)
    public SimpleResponse disableAffair(long affairMemberId) {
        boolean success;
        try {
            success = affairService.disableAffair(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId());
            if(success) return SimpleResponse.ok("失效成功");
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }

        return SimpleResponse.error("因为某些奇怪的原因失败了");
    }

    @ApiOperation(value = "更新封面",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/update_covers", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.EDIT_AFFAIR_INFO)
    public SimpleResponse updateCovers(String coverList , Long affairMemberId ) {
        if(coverList == null){
            return SimpleResponse.error("coverList不能为空");
        }
        boolean result = affairService.updateCovers(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId(),coverList);
        if(result == false){
            return SimpleResponse.error("添加失败");
        }
        return SimpleResponse.ok("添加成功");
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


    @ApiOperation(value = "得到事务树",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/get_tree", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getTree(Long allianceId) {
        if(allianceId == null){
            return SimpleResponse.error("allianceId不能为空");
        }
        return SimpleResponse.ok(affairService.getAffairTree(allianceId));
    }

    @ApiOperation(value = "得到事务树",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/get_tree_by_user", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getTree() {
        return SimpleResponse.ok(affairService.getAffairTreeByUser());
    }

    @ApiOperation(value = "移动事务",response = String.class,notes = "拥有权限,返回值中,0表示失败,1表示正在等待审核,2表示成功")
    @RequestMapping(value = "/move_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.MOVE_AFFAIR)
    public SimpleResponse moveAffair(Long affairMemberId , Long targetAffairId ) {
        if(affairMemberId == null || targetAffairId == null){
            return SimpleResponse.error("参数不能为空");
        }
        try{
            return SimpleResponse.ok(affairService.moveAffair(GlobalValue.currentAllianceId(),GlobalValue.currentAffairId(),targetAffairId,GlobalValue.currentRoleId()));
        }catch(Exception e){
            e.printStackTrace();
            return SimpleResponse.ok(AffairMoveState.FAIL);
        }

    }

    @ApiOperation(value = "处理移动事务",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/handle_move_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ACCEPT_MOVED_AFFAIR)
    public SimpleResponse handleMoveAffair(Long allianceId ,  Long affairId , Long targetAffairId , Long roleId , boolean isAgree) {
        if(allianceId == null || targetAffairId == null || affairId == null || roleId == null){
            return SimpleResponse.error("参数不能为空");
        }
        boolean result = affairService.handleMoveAffair(allianceId,affairId,targetAffairId,roleId,isAgree);
        return SimpleResponse.ok(result);

    }

    @ApiOperation(value = "切换事务角色",response = String.class,notes = "拥有权限")
    @RequestMapping(value = "/switch_role", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.CHECK_AFFAIR_HOMEPAGE)
    public SimpleResponse switchRole(Long affairMemberId, Long newRoleId) {
        if( newRoleId == null ){
            return SimpleResponse.error("参数不能为空");
        }
        boolean result = affairService.switchRole(GlobalValue.currentAffairId(),GlobalValue.currentAllianceId(),newRoleId);
        return SimpleResponse.ok(result);
    }

    public SimpleResponse getOutAllianceAffair(){
        return null;
    }

}
