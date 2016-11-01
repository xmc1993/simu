package service;

import cn.superid.webapp.notice.proto.Message.NoticeMsg;
import cn.superid.webapp.service.IMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xmc1993 on 16/9/12.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class MessageServiceTest {
    @Autowired
    IMessageService messageService;

    @Test
    public void testPublishMessage() throws IOException {
        NoticeMsg.Builder noticeMsg = NoticeMsg.newBuilder();
        noticeMsg.setType("测试");
        noticeMsg.setContent("i am ");
        noticeMsg.setAffairId(12345L);
        noticeMsg.setDisplayName("xmc1993");
        NoticeMsg notice = noticeMsg.build();
        FileOutputStream output = new FileOutputStream("src/test.book");
        notice.writeTo(output);
        output.close();
        System.out.println("over");
    }



}
