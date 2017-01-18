package cn.superid.webapp.notice;

import cn.superid.webapp.controller.VO.NoticeVO;
import cn.superid.webapp.enums.NoticeType;
import cn.superid.webapp.enums.state.LinkType;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.model.NoticeEntity;
import cn.superid.webapp.utils.TimeUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Created by jessiechen on 17/01/17.
 */
public class NoticeGenerator {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final String ALLIANCE_INVATATION = "%s%s邀请您加入盟%s，立即处理";//M1 角色名+用户名+邀请您加入盟+盟名称，立即处理

    //生成邀请加入盟的消息通知
    public static NoticeVO getAllianceInvitation(long userId, long invitationId, long allianceId, String allianceName, long inviterId, String inviterName, String inviterRoleTitle) throws Exception {
        NoticeVO noticeVO = getGeneral(userId);
        noticeVO.setType(NoticeType.ALLIANCE_INVITATION);
        String content = String.format(ALLIANCE_INVATATION, inviterRoleTitle, inviterName, allianceName);
        Link[] links = new Link[3];
        int begin = inviterRoleTitle.length();
        Link userLink = new Link(LinkType.USER, begin, begin + inviterName.length() - 1, inviterId);
        links[0] = userLink;
        begin += inviterName.length() + 6;
        Link allianceLink = new Link(LinkType.ALLIANCE, begin, begin + allianceName.length() - 1, allianceId);
        links[1] = allianceLink;
        begin += allianceName.length() + 1;
        Link invitationLink = new Link(LinkType.INVITATION, begin, begin + 3, inviterId);
        links[2] = invitationLink;
        noticeVO.setContent(content);
        noticeVO.setUrls(links);
        return noticeVO;
    }

    private static NoticeVO getGeneral(long userId) {
        NoticeVO noticeVO = new NoticeVO();
        noticeVO.setCreateTime(TimeUtil.getCurrentSqlTime());
        noticeVO.setUserId(userId);
        noticeVO.setState(ValidState.Valid);
        return noticeVO;
    }
}
