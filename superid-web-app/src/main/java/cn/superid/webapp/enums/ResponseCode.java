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
     int CatchException =250;
     int Error = -1;
}
