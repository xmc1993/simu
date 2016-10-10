package util;

import org.junit.Assert;
import org.junit.runners.model.InitializationError;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequestEvent;
import javax.validation.constraints.AssertTrue;
import java.io.FileNotFoundException;

public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {
    static {
        try {
            Log4jConfigurer.initLogging("classpath:META-INF/log4j.properties");


        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
    }

    public static void setSessionAttr(String key,Object value){
         assert RequestContextHolder.getRequestAttributes()!=null;
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().setAttribute(key,value);
    }

    public JUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        RequestContextListener listener = new RequestContextListener();
        MockServletContext context = new MockServletContext();
        MockHttpServletRequest request = new MockHttpServletRequest(context);
        request.getSession().setAttribute("userId",108);
        listener.requestInitialized(new ServletRequestEvent(context, request));
        assert RequestContextHolder.getRequestAttributes()!=null;

    }
}
