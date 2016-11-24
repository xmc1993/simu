package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.VO.SearchUserVO;
import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.security.AlliancePermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.IUserService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by jizhenya on 16/11/24.
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "搜索用户", response = boolean.class, notes = "在盟内需要权限的接口都要返回roleId")
    @RequestMapping(value = "/search_user",method = RequestMethod.GET)
    @RequiredPermissions(alliance = AlliancePermissions.ManageUser)
    public SimpleResponse searchUser(Long roleId , String input){

        return SimpleResponse.ok(roleService.searchUser(GlobalValue.currentAllianceId(),input));
    }

    @ApiOperation(value = "添加盟成员", response = boolean.class, notes = "在盟内需要权限的接口都要返回roleId")
    @RequestMapping(value = "/add_alliance_user",method = RequestMethod.GET)
    @RequiredPermissions(alliance = AlliancePermissions.ManageUser)
    public SimpleResponse addAllianceUser(Long roleId , List<AddAllianceUserForm> users){

        return SimpleResponse.ok(roleService.addAllianceUser(users,GlobalValue.currentAllianceId()));
    }

}
