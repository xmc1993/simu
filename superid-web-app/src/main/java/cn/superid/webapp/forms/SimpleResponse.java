package cn.superid.webapp.forms;


import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.enums.ResponseCode;

/**
 * Created by zoowii on 14/10/13.
 */
public class SimpleResponse {
    private int code;
    private Object data;

    public SimpleResponse() {
        this.code = ResponseCode.OK;
        this.data = "Success";
    }

    public SimpleResponse(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public SimpleResponse setError(int code, String errorMessage) {
        this.code = code;
        this.data = errorMessage;
        return this;
    }

    public static SimpleResponse exception(Exception exception) {
        return new SimpleResponse(ResponseCode.CatchException, exception.getMessage());
    }

    public static SimpleResponse error(Object data) {
        return new SimpleResponse(ResponseCode.Error, data);
    }



    public static SimpleResponse ok(Object data) {
        return new SimpleResponse(ResponseCode.OK, data);
    }

}
