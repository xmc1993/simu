package cn.superid.webapp.security;

import cn.superid.webapp.model.UserEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zp on 2016/7/25.
 */
public interface IAuth {
    public void authUser(Long userId, String chatToken);

    public void unAuthUser();

    public UserEntity currentUser();

    public Long currentUserId();

    public boolean  isAuthenticated();

    public void setSessionAttr(String key,Object v);


    public Object getSessionAttr(String key);

    public HttpServletRequest getRequest();


}
