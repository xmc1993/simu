package cn.superid.webapp.controller;

import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.INoticeService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jizhenya on 17/1/17.
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @ApiOperation(value = "接受邀请", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/get_list", method = RequestMethod.GET)
    public SimpleResponse getToken() {
        return  SimpleResponse.ok(noticeService.getInvitationList());
    }




}
