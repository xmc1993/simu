package cn.superid.webapp.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by xiaofengxu on 16/10/8.
 */

public class GlobalValue {
    public static HttpSession getCurrentHttpSession() {
        HttpServletRequest request = getRequest();
        HttpSession session =request==null?null:request.getSession(true);
        return session;
    }


    public static HttpServletRequest getRequest() {
        if((RequestContextHolder.getRequestAttributes())==null) {
            return null;
        }
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static long currentAllianceId(){
        long allianceId =  (long)getCurrentHttpSession().getAttribute("allianceId");
        if(allianceId==0){
            throw new RuntimeException("can't get allianceId");
        }
        return allianceId;

    }

    public static long currentAffairId(){
        long affairId = (long)getCurrentHttpSession().getAttribute("affairId");
        if(affairId==0){
            throw new RuntimeException("can't get affairId");

        }
        return affairId;
    }

    public static long currentRoleId(){
        long roleId = (long)getCurrentHttpSession().getAttribute("roleId");
        if(roleId==0){
            throw new RuntimeException("can't get affairId");
        }
        return roleId;
    }

    public static long currentUserId(){
        return (long) getCurrentHttpSession().getAttribute("userId");
    }

}
