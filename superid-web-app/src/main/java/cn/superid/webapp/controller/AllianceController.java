package cn.superid.webapp.controller;

import cn.superid.utils.StringUtil;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by xiaofengxu on 16/9/8.
 */
@Controller
@RequestMapping("/alliance")
public class AllianceController {

    @Autowired
    private IAllianceService allianceService;


    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/createAlliance", method = RequestMethod.POST)
    public SimpleResponse createAlliance(AllianceCreateForm allianceCreateForm) {
        String name = allianceCreateForm.getName();

        if (StringUtil.isEmpty(name)||allianceService.validName(name)) {
            return SimpleResponse.error("error_name");
        }

//        try {
//            allianceEntity = allianceService.createAlliance(allianceCreateForm);
//        } catch (UnityServiceException e) {
//            return new SimpleResponse(ErrorCodes.CATCH_EXCEPTION, e.getMessage());
//        }
//        return new SimpleResponse(SimpleResponse.OK, allianceEntity);
        return null;
    }

}
