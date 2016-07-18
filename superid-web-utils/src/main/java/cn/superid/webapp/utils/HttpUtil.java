package cn.superid.webapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Created by zoowii on 2015/3/12.
 */
public class HttpUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);
    
    public static void safeWrite(HttpServletResponseWrapper response, CharSequence str){
        try {
            response.getWriter().append(str);
        } catch (IOException e) {
            LOG.error("io error", e);
        }
    }
}
