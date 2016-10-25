package cn.superid.admin.form;

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
}
