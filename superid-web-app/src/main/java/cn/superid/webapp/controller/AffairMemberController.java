package cn.superid.webapp.controller;

import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.IAffairMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by njuTms on 16/9/14.
 */
@Controller
@RequestMapping(value = "affair_member")
public class AffairMemberController {
    @Autowired
    private IAffairMemberService affairMemberService;

    public SimpleResponse addMember(Long allianceId,Long affairId, Long roleId,  String permissions,long permissionGroupId){
        try {
            return SimpleResponse.ok(affairMemberService.addMember(allianceId,affairId,roleId,permissions,permissionGroupId));
        }catch (Exception e){
            return SimpleResponse.error("添加成员失败");
        }

    }
}
