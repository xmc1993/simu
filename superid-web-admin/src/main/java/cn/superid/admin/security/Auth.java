package cn.superid.admin.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by njuTms on 16/10/9.
 */
@Component
public class Auth implements MyAuth {
    @Override
    public void authUser(String userName) {
        HttpSession httpSession = getCurrentHttpSession();
        httpSession.setAttribute("admin", userName);
    }

    public void unAuthUser(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(true);
        unAuthUser(httpSession);
    }


    public void unAuthUser(HttpSession session) {
        session.removeAttribute("admin");
    }

    @Override
    public void unAuthUser() {

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
    public void setSessionAttr(String key, Object v) {
        getCurrentHttpSession().setAttribute(key,v);
    }

    @Override
    public boolean isAuthenticated() {
        String userName = (String)getCurrentHttpSession().getAttribute("admin");
        return userName!=null;
    }

    @Override
    public Object getSessionAttr(String key) {
        return getCurrentHttpSession().getAttribute(key);
    }
}
