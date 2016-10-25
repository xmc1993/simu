package cn.superid.admin.security;


import cn.superid.admin.annotation.NotLogin;
import cn.superid.admin.form.ResponseCode;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class AdminInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AdminInterceptor.class);
    private static final int notLogin = 0;
    private static final int notPermitted = 1;
    private static final int hasPermission = 2;

    @Autowired
    private MyAuth auth;

    private static final Map<String, Object> serviceMethodNotLoginInfoMapping = new HashMap<>();
    private static final Lock serviceMethodNotLoginInfoLock = new ReentrantLock();


    private static String getHandlerMethodSignature(HandlerMethod handlerMethod) {
        StringBuilder builder = new StringBuilder();
        builder.append(handlerMethod.getBeanType().getCanonicalName());
        builder.append(".");
        builder.append(handlerMethod.getMethod().getName());
        builder.append("(");
        for (int i = 0; i < handlerMethod.getMethodParameters().length; ++i) {
            MethodParameter methodParameter = handlerMethod.getMethodParameters()[i];
            if (i > 0) {
                builder.append(",");
            }
            builder.append(methodParameter.getParameterType().getCanonicalName());
        }
        builder.append(")");
        return builder.toString();
    }

    private <T extends Annotation> T getAnnotationFromHandlerMethodWithCache(HandlerMethod handlerMethod, Class<T> annotationCls, Map<String, Object> mapping, Lock lock) {
        // 首先判断此方法是否有@annotationCls,并cache这个元信息避免重新判断
        String signatureStr = getHandlerMethodSignature(handlerMethod);
        Object methodCacheInfoObj;
        methodCacheInfoObj = mapping.get(signatureStr);
        if (methodCacheInfoObj != null) {
            if (annotationCls.isAssignableFrom(methodCacheInfoObj.getClass())) {
                return (T) methodCacheInfoObj;
            } else {
                return null;
            }
        }
        lock.lock();
        try {
            methodCacheInfoObj = mapping.get(signatureStr);
            if (methodCacheInfoObj != null) {
                if (annotationCls.isAssignableFrom(methodCacheInfoObj.getClass())) {
                    return (T) methodCacheInfoObj;
                } else {
                    return null;
                }
            } else {
                T anno = handlerMethod.getMethodAnnotation(annotationCls);
                if (anno == null) {
                    mapping.put(signatureStr, false);
                    return null;
                }
                mapping.put(signatureStr, anno);
                return anno;
            }
        } finally {
            lock.unlock();
        }
    }

    private NotLogin getNotLoginFromHandlerMethodWithCache(HandlerMethod handlerMethod) {
        return getAnnotationFromHandlerMethodWithCache(handlerMethod, NotLogin.class, serviceMethodNotLoginInfoMapping, serviceMethodNotLoginInfoLock);
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!(handler instanceof HandlerMethod)){
            return super.preHandle(request, response, handler);
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (getNotLoginFromHandlerMethodWithCache(handlerMethod) != null) {
            return super.preHandle(request,response,handler);
        }
        if(!auth.isAuthenticated()){
            PrintWriter writer = response.getWriter();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", ResponseCode.Unauthorized);
            jsonObject.put("data", "need login");
            writer.write(jsonObject.toJSONString());
            return false;
        }
        return super.preHandle(request, response, handler);
    }

}
