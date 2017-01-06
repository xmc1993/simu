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
    int CatchException = 2500;
    int DataBaseException = 2600;
    int Error = -1;

     //User
     int NeedVerifyCode = 15; //需要验证码
     int ErrorVerifyCode = 16; //验证码错误
     int ErrorUserNameOrPassword = 17; //用户名密码不匹配
     int HasRegistered = 18; //该账号已经注册
     int InvalidMobileFormat = 19; //手机格式不正确
     int NotRegistered = 20; //该账号未被注册
     int Frequency = -2; //访问过于频繁

     //Affair
     int AffairNotExist = 101; //事务不存在
     int MemberIsExistInAffair = 102; //该成员已经在该事务中,用于邀请事务角色时
     int WaitForDeal = 103; //等待处理
     int ApplicationNotExist = 104; //邀请不存在
     int InvitationNotExist = 105; //申请不存在
     int HasChild = 106; //事务有子事务
     int HasTrade = 107; //事务下有交易

     //Role
     int RoleNotExist = 201; //角色不存在
     int RoleNotInAlliance = 202; //暂定小学弟专用,邀请事务角色时,邀请的角色不是盟内角色
     int RoleIsInAlliance = 203; //暂定小学弟专用,邀请事务角色时,邀请的角色是盟内角色
}
