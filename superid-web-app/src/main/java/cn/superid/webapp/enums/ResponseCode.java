package cn.superid.webapp.enums;

/**
 * Created by zp on 2016/7/26.
 */
public interface ResponseCode {
    int OK = 0;
    int BadRequest = 400;
    int Unauthorized = 401;
    int Forbidden = 403;
    int NotAcceptable = 406;
    int NeedParams = 407;
    int CatchException = 2500;
    int DataBaseException = 2600;
    int Error = -1;

    //User
    int NeedVerifyCode = 15; //需要验证码
    int ErrorVerifyCode = 16; //验证码错误
    int ErrorUserNameOrPassword = 17; //用户名密码不匹配
    int HAS_REGISTERED = 18; //该账号已经注册
    int INVALID_MOBILE_FORMAT = 19; //手机格式不正确
    int NotRegistered = 20; //该账号未被注册
    int Frequency = -2; //访问过于频繁

    //Affair
    int AFFAIR_INVALID = 101; //事务不存在或者已经失效
    int MEMBER_IS_EXIST_IN_AFFAIR = 102; //该成员已经在该事务中,用于邀请事务角色时
    int WAIT_FOR_DEAL = 103; //等待处理
    int APPLICATION_NOT_EXIST = 104; //邀请不存在
    int INVITATION_INVALID = 105; //申请不存在
    int HAS_CHILD = 106; //事务有子事务
    int HAS_TRADE = 107; //事务下有交易

    //Role
    int Role_INVALID = 201; //角色不存在
    int RoleNotInAlliance = 202; //暂定小学弟专用,邀请事务角色时,邀请的角色不是盟内角色
    int ROLE_IS_IN_ALLIANCE = 203; //暂定小学弟专用,邀请事务角色时,邀请的角色是盟内角色

    //Alliance
    int NeedCertification = 301; //邀请盟成员的时候判断该盟是否已验证
}
