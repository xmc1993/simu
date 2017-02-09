package cn.superid.webapp.controller;

import cn.superid.webapp.controller.forms.RolePublicTypeForm;
import cn.superid.webapp.controller.forms.RolePublicTypeListForm;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.IRoleService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

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

    @ApiOperation(value = "得到当前用户所有角色", response = String.class, notes = "")
    @RequestMapping(value = "/roles",method = RequestMethod.GET)
    public SimpleResponse getUserRoles(){
        return new SimpleResponse(ResponseCode.OK,roleService.getRoles());
    }

    @ApiOperation(value = "", response = String.class, notes = "")
    @RequestMapping(value = "/edit_public_type",method = RequestMethod.POST)
    public SimpleResponse editPublicType(@RequestBody RolePublicTypeListForm roles){
        if(roles == null){
            return SimpleResponse.error(null);
        }
        return new SimpleResponse(ResponseCode.OK,roleService.editPublicType(roles.getRoles()));
    }


}
