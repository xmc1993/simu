package cn.superid.webapp.security.impl;


import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.security.IAuth;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SpringAuthenticator implements IAuth {

    @Override
    public void authUser(Long userId) {
        HttpSession httpSession = getCurrentHttpSession();
        httpSession.setAttribute("userId", userId);
    }

    public void unAuthUser(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(true);
        unAuthUser(httpSession);
    }


    public void unAuthUser(HttpSession session) {
        session.removeAttribute("userId");
    }

    @Override
    public void unAuthUser() {
        getCurrentHttpSession().removeAttribute("userId");
        getCurrentHttpSession().invalidate();
    }

    @Override
    public UserEntity currentUser() {
        return null;
    }

    @Override
    public Long currentUserId() {
        return (Long)getCurrentHttpSession().getAttribute("userId");
    }

    public HttpSession getCurrentHttpSession() {
        HttpServletRequest request = getRequest();
        HttpSession session =request==null?null:request.getSession(true);
        return session;
    }


    @Override
    public HttpServletRequest getRequest() {
        if((RequestContextHolder.getRequestAttributes())==null) {
            return null;
        }
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    @Override
    public boolean isAuthenticated() {
        Long c= currentUserId();
        return currentUserId() != null;
    }

    @Override
    public void setSessionAttr(String key, Object v) {
        getCurrentHttpSession().setAttribute(key,v);
    }

    @Override
    public Object getSessionAttr(String key) {
        return getCurrentHttpSession().getAttribute(key);
    }
}
