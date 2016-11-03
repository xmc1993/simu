package cn.superid.webapp.enums;

/**
 * Created by zp on 2016/7/26.
 */
public interface ResponseCode {
     int OK =0;
     int BadRequest=400;
     int  Unauthorized = 401;
     int Forbidden = 403;
     int NotAcceptable =406;
     int CatchException =2500;
     int Error = -1;
     int NeedVerifyCode = 15;
     int ErrorVerifyCode = 16;
     int ErrorUserNameOrPassword = 17;
     int HasRegistered = 18;
     int InvalidMobileFormat = 19;
     int NotRegistered = 20;
}
