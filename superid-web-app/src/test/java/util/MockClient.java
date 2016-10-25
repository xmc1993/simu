package util;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequestEvent;

/**
 * Created by xiaofengxu on 16/10/10.
 */
public class MockClient {


    public static void emitRequest(MockHttpServletRequest request){
        RequestContextListener listener = new RequestContextListener();
        MockServletContext context = new MockServletContext();
        listener.requestInitialized(new ServletRequestEvent(context, request));
    }
}
