package cn.superid.webapp.controller;

import cn.superid.webapp.controller.VO.InvitationVO;
import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.NoticeEntity;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAllianceUserService;
import cn.superid.webapp.service.INoticeService;
import cn.superid.webapp.service.impl.UserService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private IAffairMemberService affairMemberService;

    @Autowired
    private IAllianceUserService allianceUserService;

    @ApiOperation(value = "接受邀请", response = InvitationVO.class, notes = "")
    @RequestMapping(value = "/get_list", method = RequestMethod.GET)
    public SimpleResponse getToken() {
        return SimpleResponse.ok(noticeService.getInvitationList(userService.currentUserId()));
    }

    @ApiOperation(value = "查询通知列表", response = NoticeEntity.class, notes = "state 0未读 1已读，type 1@我的 2任务提醒 3 系统通知")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public SimpleResponse search(@RequestParam(required = false) Integer state,
                                 @RequestParam(required = false) Integer type) throws Exception {
        return SimpleResponse.ok(noticeService.search(userService.currentUserId(), state, type));
    }

    @ApiOperation(value = "将消息标记为已读", response = Boolean.class)
    @RequestMapping(value = "{id}/mark_as_read", method = RequestMethod.POST)
    public SimpleResponse markAsRead(@PathVariable Long id) throws Exception {
        return SimpleResponse.ok(noticeService.markAsRead(id));
    }

    @ApiOperation(value = "同意某个事务成员邀请", notes = "")
    @RequestMapping(value = "/agree_affair_invitation", method = RequestMethod.GET)
    public SimpleResponse agreeInvitation(@RequestParam() long invitationId) {
        return new SimpleResponse(affairMemberService.agreeInvitation(invitationId,""),null);
    }

    @ApiOperation(value = "接受加入盟邀请", response = boolean.class, notes = "接受邀请")
    @RequestMapping(value = "/agree_alliance_invitation", method = RequestMethod.GET)
    public SimpleResponse agreeInvitation(@RequestParam Long invitationId ) {
        return SimpleResponse.ok(allianceUserService.agreeInvitationToAlliance(invitationId));
    }

    @ApiOperation(value = "拒绝某个事务成员邀请", response = AffairRoleCard.class, notes = "")
    @RequestMapping(value = "/reject_invitation", method = RequestMethod.GET)
    public SimpleResponse rejectInvitation(@RequestParam() long invitationId,String reason) {
        return new SimpleResponse(affairMemberService.rejectInvitation(invitationId,reason),null);
    }

}
