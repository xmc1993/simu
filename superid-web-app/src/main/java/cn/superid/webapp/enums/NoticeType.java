package cn.superid.webapp.enums;

/**
 * Created by xmc1993 on 16/10/25.
 */
public class NoticeType {

    //@事件
    public static final int AT_MEMBER = 10;//@某人

    //任务相关事件，取值范围20-49
    public static final int TASK_CREATED = 20;//任务创建
    public static final int TASK_UPDATED = 21;//任务更新
    public static final int TASK_BE_DUE_TO_EXPIRE = 22;//任务将要到期
    public static final int TASK_OVERDUE = 23;//任务超期


    //盟相关事件，取值范围50-79
    public static final int ALLIANCE_JOIN_SUCCESS = 50;//加入盟成功
    public static final int ALLIANCE_FRIEND_APPLY = 51;//盟友申请
    public static final int ALLIANCE_FRIEND_APPLY_ACCEPTED = 52;//盟友申请被通过
    public static final int ALLIANCE_INVITATION = 53;// 邀请加入盟


    //事务相关事件，取值范围80-99
    public static final int AFFAIR_JOIN_SUCCESS = 80;//加入事务
    public static final int AFFAIR_MOVE_APPLY = 81;//移动事务申请
    public static final int AFFAIR_MOVE_APPLY_ACCEPTED = 82;//移动事务申请成功
    public static final int AFFAIR_MOVE_APPLY_REJECTED = 83;//移动事务申请失败
    public static final int AFFAIR_INVITATION = 84;//邀请盟外角色加入事务

}