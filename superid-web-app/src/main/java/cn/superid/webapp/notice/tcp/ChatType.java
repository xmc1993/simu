package cn.superid.webapp.notice.tcp;

/**
 * Created by jessiechen on 09/01/17.
 */
public interface ChatType {
    int SYSTEM = 1;
    int FRIEND_CHAT = 2;//好友私聊
    int GROUP = 3;//群聊
    int ANNOUNCEMENT = 4;//公告聊天
    int AFFAIR_CHAT = 5;//事务内私聊
    int TWO_ALLIANCE_CHAT = 6;
}
