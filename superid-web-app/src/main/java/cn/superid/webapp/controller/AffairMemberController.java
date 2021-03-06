package cn.superid.webapp.controller;

import cn.superid.jpa.util.Pagination;
import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.annotation.NotLogin;
import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.VO.AddAffairRoleFormVO;
import cn.superid.webapp.controller.VO.AffairUserInfoVO;
import cn.superid.webapp.controller.VO.ListVO;
import cn.superid.webapp.controller.forms.AddAffairRoleForm;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.*;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.vo.AffairMemberSearchVo;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private IAffairService affairService;

    @ApiOperation(value = "同意进入事务申请", response = AffairMemberEntity.class, notes = "拥有同意申请的权限")
    @RequestMapping(value = "/agree_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.ADD_AFFAIR_ROLE})
    public SimpleResponse agreeAffairMemberApplication(long affairMemberId, long applicationId, String dealReason) {
        int code = affairMemberService.agreeAffairMemberApplication(userService.currentUserId(), GlobalValue.currentAllianceId(),
                GlobalValue.currentAffairId(), applicationId, GlobalValue.currentRoleId(), dealReason);
        return new SimpleResponse(code, null);

    }

    @ApiOperation(value = "拒绝进入事务申请", response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/reject_affair_member_application", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.ADD_AFFAIR_ROLE})
    public SimpleResponse disagreeAffairMemberApplication(long affairMemberId, long applicationId, String dealReason) {
        int code = affairMemberService.rejectAffairMemberApplication(userService.currentUserId(), GlobalValue.currentAllianceId(),
                GlobalValue.currentAffairId(), applicationId, GlobalValue.currentRoleId(), dealReason);
        return new SimpleResponse(code, null);

    }

    @ApiOperation(value = "申请加入事务", response = String.class, notes = "")
    @RequestMapping(value = "/apply_for_enter_affair", method = RequestMethod.POST)
    public SimpleResponse applyForEnterAffair(long roleId, long allianceId, long targetAllianceId, long targetAffairId, String applyReason) {
        int code = affairMemberService.canApplyForEnterAffair(targetAllianceId, targetAffairId, roleId);
        if (code != 0) {
            return new SimpleResponse(code, null);
        }
        if (allianceId == targetAllianceId) {
            boolean isOwner = affairMemberService.isOwnerOfParentAffair(roleId, targetAffairId, targetAllianceId);
            if (isOwner) {
                //添加affairMember
                affairMemberService.addMember(targetAllianceId, targetAffairId, roleId, AffairPermissionRoleType.OWNER);
                //检测是否已经是事务成员,是的话就更新,不是就添加affairUser
                affairUserService.addAffairUser(targetAllianceId, targetAffairId, userService.currentUserId(), roleId);
                return SimpleResponse.ok(null);
            }
        }
        code = affairMemberService.applyForEnterAffair(userService.currentUserId(), targetAllianceId, targetAffairId, roleId, applyReason);
        return new SimpleResponse(code, null);
    }


    @ApiOperation(value = "邀请加入事务", response = String.class)
    @RequiredPermissions(affair = AffairPermissions.ADD_AFFAIR_ROLE)
    @RequestMapping(value = "/invite_to_enter_affair", method = RequestMethod.POST)
    public SimpleResponse inviteToEnterAffair(long affairMemberId, @RequestBody AddAffairRoleFormVO roles) {
        List<Long> allianceRoles = roles.getAllianceRoles();
        List<Long> outAllianceRoles = roles.getOutAllianceRoles();
        //邀请盟内
        int code = affairMemberService.inviteAllianceRoleToEnterAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(),
                GlobalValue.currentRoleId(), userService.currentUserId(), allianceRoles);
        if (code != 0) {
            return new SimpleResponse(code, null);
        }
        //邀请盟外
        code = affairMemberService.inviteOutAllianceRoleToEnterAffair(GlobalValue.currentAllianceId(), GlobalValue.currentAffairId(),
                GlobalValue.currentRoleId(), userService.currentUserId(), outAllianceRoles);
        return new SimpleResponse(code, null);

    }


    @ApiOperation(value = "获取某个事务的所有直系负责人", response = AffairMemberEntity.class, notes = "当要修改某个角色权限时,需要判断在不在这个负责人系列里面")
    @RequestMapping(value = "/get_directors", method = RequestMethod.GET)
    public SimpleResponse getDirectors(long affairMemberId) {
        List<Long> ids = affairMemberService.getDirectorIds(GlobalValue.currentAffairId(), GlobalValue.currentAllianceId());
        return SimpleResponse.ok(StringUtil.join(ids, ","));
    }

    @ApiOperation(value = "获取事务内的所有角色,分布加载", response = GetRoleCardsMap.class, notes = "获取的是一个list<GetRoleCardsMap>")
    @RequestMapping(value = "/get_role_cards", method = RequestMethod.POST)
    public SimpleResponse getAffairRoleCards(@RequestParam() Long roleId, @RequestParam() Long affairId, @RequestBody SearchAffairRoleConditions searchAffairRoleConditions) {
        RoleCache roleCache = RoleCache.dao.findById(roleId);//权限判断
        return SimpleResponse.ok(affairMemberService.searchAffairRoleCards(roleCache.getAllianceId(), affairId, searchAffairRoleConditions));
    }

    @ApiOperation(value = "获取不在事务的盟内外角色", response = GetRoleCardsMap.class, notes = "获取的是一个list<GetRoleCardsMap>")
    @RequestMapping(value = "/search_other_roles", method = RequestMethod.POST)
    public SimpleResponse searchOtherRoles(@RequestParam() Long allianceId, @RequestParam() Long affairId, @RequestBody SearchRoleConditions conditions) {
        if (conditions.getCount() > 100 && conditions.getCount() < 10)
            conditions.setCount(20);
        if (conditions.getPage() < 1)
            conditions.setPage(1);
        if(conditions.getSection() < 1){
            conditions.setSection(1);
        }
        Pagination pagination = new Pagination(conditions.getPage(), conditions.getCount()*conditions.getSection(), conditions.isNeedTotal());
        List<OtherRoleCard> list = affairMemberService.searchOtherRoles(affairId,allianceId,conditions,pagination);
        ListVO<OtherRoleCard> listVO = new ListVO<>(list, conditions.getPage(), conditions.getCount(), pagination.getTotal());
        return SimpleResponse.ok(listVO);
    }



    @ApiOperation(value = "获取事务内的所有成员", response = AffairRoleCard.class, notes = "包含分页")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public SimpleResponse getAffairMembers(@RequestParam() Long allianceId, @RequestParam() Long affairId, @RequestBody SearchAffairMemberConditions conditions) {
        if (conditions.getCount() > 100 && conditions.getCount() < 10)
            conditions.setCount(20);
        if (conditions.getPage() < 1)
            conditions.setPage(1);
        Pagination pagination = new Pagination(conditions.getPage(), conditions.getCount(), conditions.isNeedTotal());
        List<AffairMemberSearchVo> list = affairMemberService.searchAffairMembers(allianceId, affairId, conditions, pagination);
        ListVO<AffairMemberSearchVo> listVO = new ListVO<>(list, conditions.getPage(), conditions.getCount(), pagination.getTotal());
        return SimpleResponse.ok(listVO);
    }


    @ApiOperation(value = "获取一个用户在一个事务中的信息", response = AffairUserInfoVO.class, notes = "用于显示盟成员的整个信息,包括公开的个人信息")
    @RequestMapping(value = "/get_affair_user_info", method = RequestMethod.GET)
    public SimpleResponse getAffairUserInfo(@RequestParam() Long allianceId, @RequestParam() Long userId) {
        AffairUserInfoVO affairUserInfoVO = affairMemberService.getAffairUserInfo(allianceId, userId);
        if (affairUserInfoVO != null) {
            return SimpleResponse.ok(affairUserInfoVO);
        } else {
            return new SimpleResponse(ResponseCode.Error, null);
        }
    }

    @ApiOperation(value = "获取某个事务责任人的角色卡片", response = AffairRoleCard.class, notes = "")
    @RequestMapping(value = "/get_director_card", method = RequestMethod.GET)
    public SimpleResponse getDirectorCard(@RequestParam() long allianceId, @RequestParam() long affairId) {
        return SimpleResponse.ok(affairMemberService.getDirectorCard(allianceId, affairId));
    }



    @ApiOperation(value = "退出事务", response = String.class)
    @RequestMapping(value = "/quit_affair", method = RequestMethod.POST)
    public SimpleResponse inviteToEnterAffair(long affairMemberId) {
        return SimpleResponse.ok(true);
    }

}
