package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.forms.AffairInfo;
import cn.superid.webapp.enums.AffairSpecialCondition;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.enums.state.AffairMoveState;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.forms.ModifyAffairInfoForm;
import cn.superid.webapp.service.vo.AffairUserVO;
import com.alibaba.fastjson.JSON;
import com.sun.istack.internal.NotNull;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    private IAffairUserService affairUserService;

    @RequestMapping(value = "/get_permissions", method = RequestMethod.GET)
    public SimpleResponse getPermissionsMap() {
        return SimpleResponse.ok(JSON.toJSON(AffairPermissions.getAllAffairPermissions()));
    }

    /**
     * 增加事务,参数
     */
    @ApiOperation(value = "添加事务", response = boolean.class, notes = "凡是事务内所有操作都需要affairMemberId;返回新建的事务id")
    @RequiredPermissions(affair = AffairPermissions.CREATE_AFFAIR)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public SimpleResponse createAffair(String name, int publicType, Long affairMemberId, String logo, String description) {
        CreateAffairForm createAffairForm = new CreateAffairForm();
        createAffairForm.setPublicType(publicType);
        createAffairForm.setOperationRoleId(GlobalValue.currentRoleId());
        createAffairForm.setParentAffairId(GlobalValue.currentAffairId());
        createAffairForm.setAllianceId(GlobalValue.currentAllianceId());
        createAffairForm.setName(name);
        createAffairForm.setLogo(logo);
        createAffairForm.setDescription(description);
        createAffairForm.validate();
        try {
            AffairInfo result = affairService.createAffair(createAffairForm);
            return SimpleResponse.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.exception(e);
        }
    }


    @RequestMapping(value = "/get_direct_child_affair", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getAllDirectChildAffair(Long affairMemberId) {
        try {
            return SimpleResponse.ok(affairService.getAllDirectChildAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId()));
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }
    }

    @ApiOperation(value = "在失效或者移动等对事务的操作确认之前,比如点击移动按钮,检查该事务是否有特殊情况需要处理", response = String.class, notes = "0表示没毛病,1表示有子事务,2表示有交易")
    @RequestMapping(value = "/generate_affair", method = RequestMethod.POST)
    public SimpleResponse beforeGenerateAffair(Long allianceId, Long affairId) {
        if ((allianceId == null) || (affairId == null)) {
            return SimpleResponse.error("param can not be null");
        }
        int condition = affairService.canGenerateAffair(allianceId, affairId);
        return new SimpleResponse(condition, null);

    }

    //TODO 标签待定
    @ApiOperation(value = "修改事务信息,将修改的字段传过来即可,affairMemberId必需", response = String.class, notes = "form里包含需要修改的信息,没有修改的字段可以不传,字段名和返回的affairInfo字段名一致")
    @RequestMapping(value = "/modify_affair_info", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.EDIT_AFFAIR_INFO)
    public SimpleResponse modifyAffairInfo(long affairMemberId, @RequestBody ModifyAffairInfoForm modifyAffairInfoForm) {
        boolean isModified = affairService.modifyAffairInfo(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(), modifyAffairInfoForm);
        if (isModified) {
            return SimpleResponse.ok("edit success");
        } else {
            return SimpleResponse.error("fail");
        }

    }

    @ApiOperation(value = "失效一个事务,确认失效的时候调用", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/disable_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.INVALID_AFFAIR)
    public SimpleResponse disableAffair(long affairMemberId) {
        boolean success;
        try {
            success = affairService.disableAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId());
            if (success) return SimpleResponse.ok("disable success");
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }

        return SimpleResponse.error("");
    }

    @ApiOperation(value = "更新封面", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/update_covers", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.EDIT_AFFAIR_INFO)
    public SimpleResponse updateCovers(String coverList, Long affairMemberId) {
        if (coverList == null) {
            return SimpleResponse.error("coverList不能为空");
        }
        boolean result = affairService.updateCovers(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(), coverList);
        if (result == false) {
            return SimpleResponse.error("添加失败");
        }
        return SimpleResponse.ok("添加成功");
    }

    @ApiOperation(value = "事务概览", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/overview_affair", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse overviewAffair(Long affairMemberId) {
        Map<String, Object> rsMap = affairService.affairOverview(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId());
        return SimpleResponse.ok(rsMap);
    }

    @ApiOperation(value = "查看所有人员", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_member", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getMember(Long affairMemberId) {
        return SimpleResponse.ok(affairService.getAllRoles(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId()));
    }


    @ApiOperation(value = "获取事务首页必要的信息", response = AffairInfo.class, notes = "publicType事务公开性:0完全公开 1盟内可见 2成员可见")
    @RequestMapping(value = "/affair_info", method = RequestMethod.POST)
    public SimpleResponse getAffairInfo(@RequestParam() Long allianceId, @RequestParam() Long affairId) {
        AffairInfo affairInfo = affairService.getAffairInfo(allianceId, affairId);
        return SimpleResponse.ok(affairInfo);
    }


    @ApiOperation(value = "得到事务树", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_tree", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getTree(Long allianceId) {
        if (allianceId == null) {
            return SimpleResponse.error("allianceId不能为空");
        }
        return SimpleResponse.ok(affairService.getAffairTree(allianceId));
    }

    @ApiOperation(value = "得到事务树", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_tree_by_user", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getTree() {
        return SimpleResponse.ok(affairService.getAffairTreeByUser());
    }

    @ApiOperation(value = "移动事务", response = String.class, notes = "拥有权限,返回值中,0表示失败,1表示正在等待审核,2表示成功")
    @RequestMapping(value = "/move_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.MOVE_AFFAIR)
    public SimpleResponse moveAffair(@RequestParam() long affairMemberId, @RequestParam()long targetAffairId) {
        try {
            return SimpleResponse.ok(affairService.moveAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(), targetAffairId, GlobalValue.currentRoleId()));
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.ok(AffairMoveState.FAIL);
        }

    }

    @ApiOperation(value = "处理移动事务", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/handle_move_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ACCEPT_MOVED_AFFAIR)
    public SimpleResponse handleMoveAffair( long allianceId, long affairId, long targetAffairId, long roleId, boolean isAgree) {

        boolean result = affairService.handleMoveAffair(allianceId, affairId, targetAffairId, roleId, isAgree);
        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "切换事务角色", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/switch_role", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.CHECK_AFFAIR_HOMEPAGE)
    public SimpleResponse switchRole(Long affairMemberId, Long newRoleId) {

        boolean result = affairService.switchRole(GlobalValue.currentAffairId(), GlobalValue.currentAllianceId(), newRoleId);
        return SimpleResponse.ok(result);
    }


}
