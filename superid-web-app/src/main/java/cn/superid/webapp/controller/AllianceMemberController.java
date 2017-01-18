package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.NotLogin;
import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.VO.AddAllianceUserFormVO;
import cn.superid.webapp.controller.VO.SearchUserVO;
import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.security.AlliancePermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IAllianceUserService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.IUserService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by jizhenya on 16/11/24.
 */
@Controller
@RequestMapping("/alliance_member")
public class AllianceMemberController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAllianceService allianceService;

    @Autowired
    private IAllianceUserService allianceUserService;

    @ApiOperation(value = "搜索用户", response = boolean.class, notes = "在盟内需要权限的接口都要传入roleId")
    @RequestMapping(value = "/search_user", method = RequestMethod.POST)
    @RequiredPermissions(alliance = AlliancePermissions.ManageAllianceUserOrRole)
    public SimpleResponse searchUser(Long roleId, String keyword, Boolean containName, Boolean containTag) {

        if (containName == null | containTag == null) {
            return SimpleResponse.error("参数不全");
        }

        return SimpleResponse.ok(roleService.searchUser(GlobalValue.currentAllianceId(), keyword, containName, containTag));
    }

    @ApiOperation(value = "添加盟成员", response = boolean.class, notes = "在盟内需要权限的接口都要传入roleId")
    @RequestMapping(value = "/add_alliance_user", method = RequestMethod.POST)
    @RequiredPermissions(alliance = AlliancePermissions.ManageAllianceUserOrRole)
    public SimpleResponse addAllianceUser(@RequestBody AddAllianceUserFormVO users, long roleId) {
        long allianceId = GlobalValue.currentAllianceId();
        boolean isCertificated = allianceService.isCertificated(allianceId);
        if (isCertificated) {
            return SimpleResponse.ok(roleService.addAllianceUser(users.getUsers(), allianceId, roleId, userService.currentUserId()));
        } else {
            return new SimpleResponse(ResponseCode.NeedCertification, null);
        }
    }


    @ApiOperation(value = "获取盟中失效的角色列表", response = String.class, notes = "在盟内需要权限的接口都要传入roleId")
    @RequestMapping(value = "/invalid_roles", method = RequestMethod.GET)
    @RequiredPermissions(alliance = AlliancePermissions.ManageAllianceUserOrRole)
    public SimpleResponse getInvalidRoles(Long roleId) {
        List<RoleEntity> invalidRoles = roleService.getInvalidRoles(GlobalValue.currentAllianceId());
        return SimpleResponse.ok(invalidRoles);
    }

    @ApiOperation(value = "接受邀请", response = boolean.class, notes = "接受邀请")
    @RequestMapping(value = "/agree_invitation", method = RequestMethod.POST)
    public SimpleResponse agreeInvitation(@RequestParam Long invitationId ,@RequestParam Long alliacneId ,@RequestParam String dealReason) {
        return SimpleResponse.ok(allianceUserService.agreeInvitationToAlliance(invitationId,alliacneId,dealReason));
    }


}
