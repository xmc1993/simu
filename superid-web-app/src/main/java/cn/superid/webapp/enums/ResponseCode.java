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
    int NeedVerifyCode = 15;
    int ErrorVerifyCode = 16;
    int ErrorUserNameOrPassword = 17;
    int HasRegistered = 18;
    int InvalidMobileFormat = 19;
    int NotRegistered = 20;
    int Frequency = -2;

    //Affair
    int AffairNotExist = 101;
    int MemberIsExistInAffair = 102;
    int WaitForDeal = 103;
    int ApplicationNotExist = 104;
    int InvitationNotExist = 105;
    int HasChild = 106;
    int HasTrade = 107;

    //Role
    int RoleNotExist = 201;
    int RoleNotInAlliance = 202;
    int RoleIsInAlliance = 203;
}
