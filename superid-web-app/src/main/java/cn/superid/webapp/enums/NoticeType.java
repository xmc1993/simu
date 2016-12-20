package cn.superid.webapp.enums;

/**
 * Created by xmc1993 on 16/10/25.
 */
public class NoticeType {

    //@事件
    public static final Integer AT_MEMBER = 10;//@某人
    //任务相关事件
    public static final Integer TASK_CREATED = 20;//任务创建
    public static final Integer TASK_UPDATED = 21;//任务更新
    public static final Integer TASK_BE_DUE_TO_EXPIRE = 22;//任务将要到期
    public static final Integer TASK_OVERDUE = 23;//任务超期
    //盟相关事件
    public static final Integer ALLIANCE_JOIN_SUCCESS = 30;//加入盟
    public static final Integer ALLIANCE_FRIEND_APPLY = 31;//盟友申请
    public static final Integer ALLIANCE_FRIEND_APPLY_ACCEPTED = 32;//盟友申请被通过
    //事务相关事件
    public static final Integer AFFAIR_JOIN_SUCCESS = 40;//加入事务
    public static final Integer AFFAIR_MOVE_APPLY = 41;//移动事务申请
    public static final Integer AFFAIR_MOVE_APPLY_ACCEPTED = 42;//移动事务申请成功
    public static final Integer AFFAIR_MOVE_APPLY_REJECTED = 43;//移动事务申请失败

}