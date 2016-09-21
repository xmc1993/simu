package cn.superid.webapp.forms;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * Created by xiaofengxu on 16/9/20.
 */
@ApiModel
public class LoginForm {
    private  String token;
    private  String password;
    private String verifyCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
