package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.NotLogin;
import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.service.IAffairService;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xiaofengxu on 16/8/24.
 */
@Controller
@RequestMapping("/affair")
public class AffairController {
    @Autowired
    private IAffairService affairService;

    /**
     * 增加事务,参数
     */
    @ApiOperation(value = "添加事务", response = boolean.class, notes = "格式正确而且没有被注册")
    @RequiredPermissions(affair = AffairPermissions.CREATEAFFAIR)
    @RequestMapping(value = "/create_affair", method = RequestMethod.POST)
    public  SimpleResponse createAffair(CreateAffairForm createAffairForm){
        return null;
    }





}
