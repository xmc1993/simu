package cn.superid.webapp.controller;

import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.IRoleService;
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

    @RequestMapping(value = "/all_roles",method = RequestMethod.GET)
    public SimpleResponse getUserAllianceRoles(){
        return new SimpleResponse(ResponseCode.OK,roleService.getUserAllianceRoles());
    }
}
