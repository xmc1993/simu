package cn.superid.webapp.controller;

import cn.superid.webapp.controller.forms.RolePublicTypeForm;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.IRoleService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @ApiOperation(value = "", response = String.class, notes = "")
    @RequestMapping(value = "/edit_public_type",method = RequestMethod.GET)
    public SimpleResponse editPublicType(List<RolePublicTypeForm> roles){
        if(roles == null){
            return SimpleResponse.error(null);
        }
        return new SimpleResponse(ResponseCode.OK,roleService.editPublicType(roles));
    }
}
