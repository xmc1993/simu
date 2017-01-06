package cn.superid.webapp.util;

import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.webapp.enums.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessiechen on 06/01/17.
 */
@Service
public class ExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionResolver.class);

    private ObjectMapper objectMapper;

    public ExceptionResolver() {
        objectMapper = new ObjectMapper();
    }


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");

        Map<String, Object> map = new HashMap<>();
        if (ex instanceof JdbcRuntimeException) {
            map.put("code", ResponseCode.DataBaseException);
        } else {
            map.put("code", ResponseCode.CatchException);
        }
        try {
            response.getWriter().write(objectMapper.writeValueAsString(map));
        } catch (Exception e) {
            // ignorex
        }
        ex.printStackTrace();
        LOG.error(ex.getMessage());
        return mv;

    }
}
