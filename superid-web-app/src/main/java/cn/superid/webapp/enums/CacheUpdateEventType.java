package cn.superid.webapp.enums;

/**
 * Created by xmc1993 on 16/12/26.
 */
public class CacheUpdateEventType {
    //缓存更新事件
    public static final Integer UN_GROUP = 1;//群组解散
    public static final Integer REMOVE_MEMBER_FROM_GROUP = 2;//群组移除成员
    public static final Integer ADD_MEMBER_TO_GROUP = 3;//群组添加成员
    public static final Integer REMOVE_MEMBER_FROM_AFFAIR = 4;//事移除加成员
    public static final Integer ADD_MEMBER_TO_AFFAIR = 5;//事务添加成员
    public static final Integer DISABLE_AFFAIR = 6;//失效事务

}
