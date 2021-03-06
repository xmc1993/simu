package cn.superid.webapp.notice.chat.Constant;

/**
 * Created by jessiechen on 09/01/17.
 */
public interface C2CType {
    int MSG = 0;  //普通消息
    int PING = 1; //ping
    int PONG = 2;  //pong
    int BUMPED = 3;//用户被挤掉
    int NEW_LOGIN = 4;//某个用户新设备登录
    int STATUS = 5;//查询当前用户登录设备情况
    int REDIRECT = 6;//重定向
    int ROOM_MSG_QUERY = 7;//消息查询
    int MARK_READ_TIME = 8;//标记聊天时间
    int CHECK_INDEX = 9;//
    int RESPONSE = 10;
    int ERROR = 11;
    int SIGN_IN = 12;
    int GET_UNREAD_COUNT = 13;
    int QUERY_ANNOUNCEMENT_MSG = 14;//查询公告下面的所有聊天
    int UPDATE_CACHE = 15;
    int SYSTEM_NOTICE = 16;    //系统通知
}
