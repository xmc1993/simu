package cn.superid.webapp.controller;

import cn.superid.webapp.controller.VO.InvitationVO;
import cn.superid.webapp.controller.VO.NoticeVO;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.INoticeService;
import cn.superid.webapp.service.impl.UserService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by jizhenya on 17/1/17.
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @Autowired
    UserService userService;

    @ApiOperation(value = "接受邀请", response = InvitationVO.class, notes = "")
    @RequestMapping(value = "/get_list", method = RequestMethod.GET)
    public SimpleResponse getToken() {
        return SimpleResponse.ok(noticeService.getInvitationList(userService.currentUserId()));
    }

    @ApiOperation(value = "查询通知列表", response = NoticeVO.class, notes = "state 0未读 1已读，type 1@我的 2任务提醒 3 系统通知")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public SimpleResponse search(@RequestParam(required = false) Integer state,
                                 @RequestParam(required = false) Integer type) throws Exception {
        return SimpleResponse.ok(noticeService.search(userService.currentUserId(), state, type));
    }


}
