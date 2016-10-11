package cn.superid.webapp.security;

import cn.superid.webapp.model.cache.AffairMemberCache;
import cn.superid.webapp.model.cache.RoleCache;
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

    public static AffairMemberCache currentAffairMember(){
        AffairMemberCache affairMemberCache =  (AffairMemberCache) getCurrentHttpSession().getAttribute("affairMember");
        if(affairMemberCache==null){
            throw new RuntimeException("can't get affairMember");
        }
        return affairMemberCache;
    }


    public static RoleCache currentRole(){
        RoleCache roleCache = (RoleCache) getCurrentHttpSession().getAttribute("role");
        if(roleCache==null){
            throw new RuntimeException("can't get role");
        }
        return roleCache;
    }


    public static long currentAllianceId(){
        AffairMemberCache affairMemberCache =  (AffairMemberCache) getCurrentHttpSession().getAttribute("affairMember");
        if(affairMemberCache!=null){
            return affairMemberCache.getAllianceId();
        }
        return currentRole().getAllianceId();

    }

    public static long currentAffairId(){
        return currentAffairMember().getAffairId();
    }

    public static long currentRoleId(){
        return currentRole().getId();
    }

}
