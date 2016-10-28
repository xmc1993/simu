package cn.superid.admin.security;


import javax.servlet.http.HttpServletRequest;

/**
 * Created by zp on 2016/7/25.
 */
public interface MyAuth {
    public void authUser(String userName);

    public void unAuthUser();


    public void setSessionAttr(String key, Object v);
    public boolean  isAuthenticated();

    public Object getSessionAttr(String key);

    public HttpServletRequest getRequest();


}
