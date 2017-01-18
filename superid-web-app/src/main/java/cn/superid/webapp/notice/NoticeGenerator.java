package cn.superid.webapp.notice;

import cn.superid.webapp.enums.NoticeType;
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

    //生成邀请加入盟的消息通知
    public static NoticeEntity getAllianceInvitation(long userId, long invitationId, String allianceName, long inviterId, String inviterName, String inviterRoleTitle) throws Exception {
        NoticeEntity noticeEntity = getGeneral(userId);
        noticeEntity.setType(NoticeType.ALLIANCE_INVITATION);
        NoticeData noticeData = new NoticeData();
        noticeData.setLinkId(invitationId);
        noticeData.setAllianceName(allianceName);
        noticeData.setFromUserId(inviterId);
        noticeData.setFromUserName(inviterName);
        noticeData.setFromRoleTitle(inviterRoleTitle);
        noticeEntity.setData(objectMapper.writeValueAsString(noticeData));
        return noticeEntity;
    }

    private static NoticeEntity getGeneral(long userId) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        noticeEntity.setUserId(userId);
        noticeEntity.setState(ValidState.Valid);
        return noticeEntity;
    }

    public static class NoticeData {
        @JsonProperty("li")
        private long linkId;     //链接Id，有可能是邀请的Id，也有可能是任务的Id，根据type来具体解释
        @JsonProperty("fui")
        private long fromUserId;  //消息发起者的userId
        @JsonProperty("fun")
        private String fromUserName;  //消息发起者姓名
        @JsonProperty("frt")
        private String fromRoleTitle;  //消息发起者的职务
        @JsonProperty("aln")
        private String allianceName;  //盟名称
        @JsonProperty("tn")
        private String taskName;       //任务名称
        @JsonProperty("afn")
        private String affairName;  //事务名称

        public long getLinkId() {
            return linkId;
        }

        public void setLinkId(long linkId) {
            this.linkId = linkId;
        }

        public long getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(long fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getFromRoleTitle() {
            return fromRoleTitle;
        }

        public void setFromRoleTitle(String fromRoleTitle) {
            this.fromRoleTitle = fromRoleTitle;
        }

        public String getAllianceName() {
            return allianceName;
        }

        public void setAllianceName(String allianceName) {
            this.allianceName = allianceName;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getAffairName() {
            return affairName;
        }

        public void setAffairName(String affairName) {
            this.affairName = affairName;
        }
    }

    public static void main(String[] args) throws Exception {
        NoticeEntity noticeEntity = getAllianceInvitation(0, 1, "an", 2, "cdd", "manager");
        System.out.println(noticeEntity);
    }
}
