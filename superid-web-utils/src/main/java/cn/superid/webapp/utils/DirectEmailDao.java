package cn.superid.webapp.utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DirectEmailDao {
    private static final Logger LOG = LoggerFactory.getLogger(DirectEmailDao.class);


    private static AsyncTaskRunner asyncTaskRunner =new AsyncTaskRunner();

    public static boolean  sendEmail(String subject, String content, List<String> addresses) {
        boolean result = true;
        for(String address : addresses) {
            if(!sendEmail(subject, content, address)) {
                result = false;
            }


        }
        return result;
    }


    public static boolean sendEmail(String subject, String content, String address) {
        Email email = new HtmlEmail();
        email.setHostName("smtp.exmail.qq.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("sender@superid.cn", "ssdlh12345"));
        email.setSSLOnConnect(true);
        try {
            email.setFrom("sender@superid.cn");
            email.setSubject(subject);
            email.setCharset("UTF-8");
            email.setMsg(content);
            email.addTo(address);
            email.send();
            return true;
        } catch (EmailException e) {
            LOG.error("send email error", e);
            return false;
        }
    }


    public static void sendEmailAsync(final String subject, final String content, final List<String> addresses) {
        asyncTaskRunner.run(new Runnable() {
            @Override
            public void run() {
                sendEmail(subject, content, addresses);
            }
        });
    }


    public static void sendEmailAsync(final String subject, final String content, final String address) {
        asyncTaskRunner.run(new Runnable() {
            @Override
            public void run() {
                sendEmail(subject, content, address);
            }
        });
    }
}
