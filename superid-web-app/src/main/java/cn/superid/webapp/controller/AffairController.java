package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.VO.AffairOverviewVO;
import cn.superid.webapp.controller.forms.AffairInfo;
import cn.superid.webapp.enums.state.AffairMoveState;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.forms.ModifyAffairInfoForm;
import cn.superid.webapp.service.vo.AffairTreeVO;
import com.alibaba.fastjson.JSON;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by xiaofengxu on 16/8/24.
 */

@Controller
@RequestMapping("/affair")
public class AffairController {
    @Autowired
    private IAffairService affairService;

    @RequestMapping(value = "/get_permissions", method = RequestMethod.GET)
    public SimpleResponse getPermissionsMap() {
        return SimpleResponse.ok(JSON.toJSON(AffairPermissions.getAllAffairPermissions()));
    }

    /**
     * 增加事务,参数
     */
    @ApiOperation(value = "添加事务", response = AffairInfo.class, notes = "凡是事务内所有操作都需要affairMemberId;返回新建的事务id")
    @RequiredPermissions(affair = AffairPermissions.CREATE_AFFAIR)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public SimpleResponse createAffair(@RequestParam Long affairMemberId, @RequestBody CreateAffairForm createAffairForm) {
        createAffairForm.validate();
        createAffairForm.setOperationRoleId(GlobalValue.currentRoleId());
        createAffairForm.setParentAffairId(GlobalValue.currentAffairId());
        createAffairForm.setAllianceId(GlobalValue.currentAllianceId());
        try {
            AffairInfo result = affairService.createAffair(createAffairForm);
            return SimpleResponse.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.exception(e);
        }
    }

    @ApiOperation(value = "获取直接子事务", response = List.class, notes = "暂时未用")
    @RequestMapping(value = "/direct_children", method = RequestMethod.GET)
    @RequiredPermissions(affair = AffairPermissions.CHILD_AFFAIR)
    public SimpleResponse getAllDirectChildAffair(@RequestParam Long affairMemberId) {
        try {
            return SimpleResponse.ok(affairService.getAllDirectChildAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId()));
        } catch (Exception e) {
            return SimpleResponse.exception(e);
        }
    }

    @ApiOperation(value = "在失效或者移动等对事务的操作确认之前,比如点击移动按钮,检查该事务是否有特殊情况需要处理", response = Integer.class, notes = "0表示没毛病,1表示有子事务,2表示有交易")
    @RequestMapping(value = "/can_operate", method = RequestMethod.GET)
    public SimpleResponse beforeGenerateAffair(@RequestParam Long allianceId, @RequestParam Long affairId) {
        int condition = affairService.canGenerateAffair(allianceId, affairId);
        return SimpleResponse.ok(condition);
    }

    //TODO 标签待定
    @ApiOperation(value = "修改事务信息,将修改的字段传过来即可,affairMemberId必需", response = String.class, notes = "form里包含需要修改的信息,没有修改的字段可以不传,字段名和返回的affairInfo字段名一致")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.EDIT_AFFAIR_INFO)
    public SimpleResponse modifyAffairInfo(@RequestParam Long affairMemberId, @RequestBody ModifyAffairInfoForm modifyAffairInfoForm) {
        boolean isModified = affairService.modifyAffairInfo(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(), modifyAffairInfoForm);
        if (isModified) {
            return SimpleResponse.ok("edit success");
        } else {
            return SimpleResponse.error("fail");
        }

    }

    @ApiOperation(value = "失效一个事务,调用前必须先确认", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.INVALID_AFFAIR)
    public SimpleResponse disableAffair(@RequestParam long affairMemberId) {
        if (affairService.disableAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId()))
            return SimpleResponse.ok("disable success");
        else
            return SimpleResponse.error("");
    }

    @ApiOperation(value = "更新封面", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/update_covers", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.EDIT_AFFAIR_INFO)
    public SimpleResponse updateCovers(@RequestParam Long affairMemberId, @RequestParam String coverList) {
        if (affairService.updateCovers(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(), coverList))
            return SimpleResponse.ok("添加成功");
        else
            return SimpleResponse.error("添加失败");
    }

    @ApiOperation(value = "事务概览", response = AffairOverviewVO.class, notes = "拥有权限")
    @RequestMapping(value = "/overview", method = RequestMethod.GET)
    @RequiredPermissions()
    public SimpleResponse overviewAffair(@RequestParam Long affairMemberId) {
        AffairOverviewVO vo = affairService.affairOverview(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId());
        return SimpleResponse.ok(vo);
    }

    @ApiOperation(value = "查看所有人员", response = List.class, notes = "拥有权限")
    @RequestMapping(value = "/members", method = RequestMethod.GET)
    @RequiredPermissions()
    public SimpleResponse getMembers(@RequestParam Long affairMemberId) {
        return SimpleResponse.ok(affairService.getAllRoles(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId()));
    }

    @ApiOperation(value = "获取事务首页必要的信息", response = AffairInfo.class, notes = "publicType事务公开性:0完全公开 1盟内可见 2成员可见")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public SimpleResponse getAffairInfo(@RequestParam Long allianceId, @RequestParam Long affairId) {
        AffairInfo affairInfo = affairService.getAffairInfo(allianceId, affairId);
        return SimpleResponse.ok(affairInfo);
    }


    @ApiOperation(value = "得到事务树", response = AffairTreeVO.class, notes = "拥有权限")
    @RequestMapping(value = "/get_tree", method = RequestMethod.GET)
    @RequiredPermissions()
    public SimpleResponse getTree(@RequestParam Long allianceId) {
        return SimpleResponse.ok(affairService.getAffairTree(allianceId));
    }

    @ApiOperation(value = "根据用户得到事务树", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_tree_by_user", method = RequestMethod.GET)
    @RequiredPermissions()
    public SimpleResponse getTree() {
        return SimpleResponse.ok(affairService.getAffairTreeByUser());
    }

    @ApiOperation(value = "移动事务", response = Integer.class, notes = "拥有权限,返回值中,0表示失败,1表示正在等待审核,2表示成功")
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.MOVE_AFFAIR)
    public SimpleResponse moveAffair(@RequestParam Long affairMemberId, @RequestParam Long targetAffairId) {
        try {
            return SimpleResponse.ok(affairService.moveAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(), targetAffairId, GlobalValue.currentRoleId()));
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.ok(AffairMoveState.FAIL);
        }
    }

    @ApiOperation(value = "置顶事务", response = Boolean.class, notes = "放哪还未确定")
    @RequestMapping(value = "/stick_affair", method = RequestMethod.POST)
    public SimpleResponse stickAffair(@RequestParam Long allianceId, @RequestParam Long affairId, @RequestParam boolean isStuck) {
        return SimpleResponse.ok(affairService.stickAffair(allianceId,affairId,isStuck));
    }

    @ApiOperation(value = "设置主页", response = Boolean.class, notes = "放哪还未确定")
    @RequestMapping(value = "/set_homepage", method = RequestMethod.POST)
    public SimpleResponse setHomepage(@RequestParam Long allianceId, @RequestParam Long affairId) {
        return SimpleResponse.ok(affairService.setHomepage(affairId));
    }

    @ApiOperation(value = "处理移动事务", response = Boolean.class, notes = "拥有权限")
    @RequestMapping(value = "/handle_move_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ACCEPT_MOVED_AFFAIR)
    public SimpleResponse handleMoveAffair(@RequestParam Long allianceId, @RequestParam Long affairId,
                                           @RequestParam Long targetAffairId, @RequestParam Long roleId, @RequestParam Boolean isAgree) {
        return SimpleResponse.ok(affairService.handleMoveAffair(allianceId, affairId, targetAffairId, roleId, isAgree));
    }

    @ApiOperation(value = "切换事务角色", response = Boolean.class, notes = "拥有权限")
    @RequestMapping(value = "/switch_role", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.CHECK_AFFAIR_HOMEPAGE)
    public SimpleResponse switchRole(@RequestParam Long affairMemberId, @RequestParam Long newRoleId) {
        return SimpleResponse.ok(affairService.switchRole(GlobalValue.currentAffairId(), GlobalValue.currentAllianceId(), newRoleId));
    }
}
