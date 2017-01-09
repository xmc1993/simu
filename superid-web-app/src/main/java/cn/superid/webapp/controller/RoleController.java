package cn.superid.webapp.controller;

import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.IRoleService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by njuTms on 17/1/4.
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "得到当前用户所有盟,所有角色", response = String.class, notes = "")
    @RequestMapping(value = "/all_roles",method = RequestMethod.GET)
    public SimpleResponse getUserAllianceRoles(){
        return new SimpleResponse(ResponseCode.OK,roleService.getUserAllianceRoles());
    }





}
