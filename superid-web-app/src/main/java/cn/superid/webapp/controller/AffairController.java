package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.service.IAffairService;
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @RequiredPermissions(affair = {AffairPermissions.AddAffair,AffairPermissions.AddNotice})
    public SimpleResponse CreateAffair(HttpServletRequest request, String token){

        return null;

    }

}
