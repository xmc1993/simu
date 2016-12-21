package cn.superid.admin.controller;

import cn.superid.admin.annotation.NotLogin;
import cn.superid.admin.form.SimpleResponse;
import cn.superid.admin.form.UserForm;
import cn.superid.admin.model.AdminEntity;
import cn.superid.admin.service.IAdminService;
import cn.superid.admin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by njuTms on 16/10/9.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @NotLogin
    public SimpleResponse login(@RequestBody UserForm userForm){
        AdminEntity adminEntity = adminService.login(userForm.getUserName(),userForm.getPassword());
        if(adminEntity!=null)
            String token = JwtUtil.createJwt(adminEntity.getName(),adminEntity.getId(),adminEntity.getName(),10000L,)
            return SimpleResponse.ok("success");
        else
            return SimpleResponse.error("fail");
    }

}
