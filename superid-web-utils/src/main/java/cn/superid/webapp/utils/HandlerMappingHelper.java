package cn.superid.webapp.utils;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zoowii on 15/6/13.
 */
@Component
public class HandlerMappingHelper {
    private Map<String, HandlerMapping> cachedHandlerMappings = null;
    public Map<String, HandlerMapping> getHandlerMappings(HttpServletRequest request) {
        if(cachedHandlerMappings==null) {
            synchronized (HandlerMappingHelper.class) {
                if(cachedHandlerMappings==null) {
                    WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
                    cachedHandlerMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                            appContext, HandlerMapping.class, true, false);
                }
            }
        }
        return cachedHandlerMappings;
    }
}
