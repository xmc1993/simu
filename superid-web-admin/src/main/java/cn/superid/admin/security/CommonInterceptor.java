package cn.superid.admin.security;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", (origin != null && !"null".equals(origin)) ? origin : "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,X_Requested_With");
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        /*
        if (modelAndView != null) {
            Object[] keyset = modelAndView.getModel().keySet().toArray();
            for (Object key : keyset) {
                if (!"simpleResponse".equals(key)) {
                    modelAndView.getModel().remove(key);
                }
            }
        }
        */
        super.postHandle(request, response, handler, modelAndView);
    }
}
