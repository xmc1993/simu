package cn.superid.webapp.enums;

/**
 * Created by zp on 2016/7/26.
 */
public interface ResponseCode {
    static int OK =0;
    static int BadRequest=400;
    static int  Unauthorized = 401;
    static int Forbidden = 403;
    static int NotAcceptable =406;
    static int CatchException =250;
    static int Error = -1;
}
