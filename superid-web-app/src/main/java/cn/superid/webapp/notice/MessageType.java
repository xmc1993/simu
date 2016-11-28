package cn.superid.webapp.notice;

/**
 * Created by xmc1993 on 16/11/16.
 */
public class MessageType {
    //任务提醒
    public static final Integer TASK_CREATE = 1;
    public static final Integer TASK_UPDATE = 2;
    public static final Integer TASK_EXPIRE = 3;
    public static final Integer TASK_OVERDUE = 4;

    //系统通知
    public static final Integer ALLIACNCE_JOIN = 11;
    public static final Integer ALLIACNCE_AFFAIR_JOIN = 12;
    public static final Integer ALLIACNCE_FRIEND_REQUEST = 13;
    public static final Integer ALLIACNCE_FRIEND_REQUEST_PASS = 14;
    public static final Integer ALLIACNCE_AFFAIR_MOVE_APPLY = 15;
    public static final Integer ALLIACNCE_AFFAIR_MOVE_APPLY_SUCCESS = 16;
    public static final Integer ALLIACNCE_AFFAIR_MOVE_APPLY_FAIL = 17;



}
