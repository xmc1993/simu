package cn.superid.webapp.notice;

import cn.superid.webapp.enums.NoticeType;
import cn.superid.webapp.enums.state.LinkType;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.model.*;
import cn.superid.webapp.utils.TimeUtil;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jessiechen on 17/01/17.
 */
public class NoticeGenerator {

    private static final String ALLIANCE_INVITATION = "% ? 邀请您加入盟?, ?";//M1 角色名+用户名+邀请您加入盟+盟名称，立即处理 %不需要链接
    private static final String AFFAIR_INVITATION = "% ?邀请您的角色 % % 加入盟? ?，?";//角色名＋用户名＋邀请您加入＋盟名称＋事务名称＋，立即处理


    //生成邀请加入盟的消息通知 TODO 缓存批量查询

    public static NoticeEntity generateAllianceInvitationNotice(InvitationEntity invitationEntity) {
        long inviteUserId = invitationEntity.getInviteUserId();
        long allianceId = invitationEntity.getAllianceId();
        long inviteRoleId = invitationEntity.getInviteRoleId();
        UserEntity user = UserEntity.dao.id(inviteUserId).selectOne("username");
        AllianceEntity alliance = AllianceEntity.dao.id(allianceId).selectOne("name");
        RoleEntity role = RoleEntity.dao.partitionId(allianceId).id(inviteRoleId).selectOne("title");
        if (user == null || alliance == null || role == null) {
            return null;
        }

        NoticeEntity noticeVO = getGeneral(invitationEntity.getBeInvitedUserId());
        noticeVO.setType(NoticeType.ALLIANCE_INVITATION);
        List<Link> links = generate(noticeVO,ALLIANCE_INVITATION,role.getTitle(),user.getUsername(),alliance.getName(),"立即处理");
        Link tmp = links.get(0);
        tmp.setType(LinkType.USER);
        tmp.setId(inviteUserId);

        tmp =links.get(1);
        tmp.setType(LinkType.ALLIANCE);
        tmp.setId(allianceId);

        tmp =links.get(2);
        tmp.setType(LinkType.INVITATION);
        tmp.setId(invitationEntity.getId());

        noticeVO.setUrls(JSON.toJSONString(links));
        return noticeVO;
    }


    public static List<Link> generate(NoticeEntity noticeEntity,String format, String... params) {
        StringBuilder result = new StringBuilder();
        List<Link> links = new ArrayList<>();
        int paramIndex=0;
        int length = format.length();
        int begin,end;
        for(int i=0;i<length;i++){
            char tmp = format.charAt(i);
            if(tmp=='?'){
                begin = result.length();
                result.append(params[paramIndex++]);
                end = result.length();
                Link link = new Link(begin,end);
                links.add(link);

            }else if(tmp=='%'){
                result.append(params[paramIndex++]);
            }
            else{
                result.append(tmp);
            }
        }
        noticeEntity.setContent(result.toString());
        return links;


    }

    //% ?邀请您的角色 % % 加入盟? ?，立即处理 TODO 缓存批量查询
    public static NoticeEntity generateAffairInvitationNotice(InvitationEntity invitation) {
        UserEntity user = UserEntity.dao.id(invitation.getInviteUserId()).selectOne("username");
        AllianceEntity alliance = AllianceEntity.dao.id(invitation.getAllianceId()).selectOne("name");
        RoleEntity role = RoleEntity.dao.partitionId(invitation.getAllianceId()).id(invitation.getInviteRoleId()).selectOne("title");
        RoleEntity invitedRole = RoleEntity.dao.partitionId(invitation.getAllianceId()).id(invitation.getInviteRoleId()).selectOne("title","allianceId");
        AffairEntity affair = AffairEntity.dao.partitionId(invitation.getAllianceId()).id(invitation.getAffairId()).selectOne("name");
        AllianceEntity invitedAlliance = AllianceEntity.dao.id(invitedRole.getAllianceId()).selectOne("name");

        if(user==null||alliance==null||role==null||invitedRole==null||affair==null||invitedAlliance==null){
            return null;
        }

        NoticeEntity noticeVO = getGeneral(invitation.getBeInvitedUserId());
        noticeVO.setType(NoticeType.AFFAIR_INVITATION);

        List<Link> links = generate(noticeVO,AFFAIR_INVITATION,role.getTitle(),user.getUsername(),invitedAlliance.getName(),invitedRole.getTitle(),alliance.getName(),affair.getName(),"立即处理");
        Link tmp = links.get(0);
        tmp.setType(LinkType.USER);
        tmp.setId(invitation.getInviteUserId());

        tmp =links.get(1);
        tmp.setType(LinkType.ALLIANCE);
        tmp.setId(alliance.getId());


        tmp =links.get(2);
        tmp.setType(LinkType.AFFAIR);
        tmp.setId(invitation.getAffairId());

        tmp = links.get(3);
        tmp.setType(LinkType.INVITATION);
        tmp.setId(invitation.getId());

        noticeVO.setUrls(JSON.toJSONString(links));

        return noticeVO;
    }

    private static NoticeEntity getGeneral(long userId) {
        NoticeEntity noticeVO = new NoticeEntity();
        noticeVO.setCreateTime(TimeUtil.getCurrentSqlTime());
        noticeVO.setUserId(userId);
        noticeVO.setState(ValidState.Valid);
        return noticeVO;
    }

}
