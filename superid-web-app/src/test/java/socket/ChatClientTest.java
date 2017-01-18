package socket;

import cn.superid.webapp.notice.chat.ChatClient;
import cn.superid.webapp.notice.chat.Constant.MessageSubType;
import cn.superid.webapp.notice.chat.Constant.MessageType;
import cn.superid.webapp.notice.chat.MessageAsyncRequestHandler;
import cn.superid.webapp.notice.chat.SimpleMessageHandler;
import cn.superid.webapp.notice.chat.proto.Message;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;


/**
 * Created by jessiechen on 10/01/17.
 */
public class ChatClientTest {
    long userId = 1912l;
    long roleId = 3676l;
    long toUserId = 1911l;
    long toRoleId = 3675l;
    long affairId = 7620l;
    private String host = "localhost";
    private int port = 10110;

//    @Before
//    public void loginServer() throws Exception {
//        ChatClient chatClient = ChatClient.getSingleInstance();
//        chatClient.connectChatServer("localhost", 7040, userId, "xxx", new SimpleMessageHandler());
//    }

//    @Test
//    public void sendNormalMsg() throws Exception {
//        ChatClient chatClient = ChatClient.getSingleInstance();
//        int counter = 1;
//        while (true) {
//            String name = "test";
//            String content = "count" + counter++;
//            Message message = Message.getAffairChatMsg(MessageSubType.DEFAULT, affairId, userId, roleId, toUserId, toRoleId, name, content);
//            chatClient.sendMessage(message, new MessageAsyncRequestHandler());
//            Thread.sleep(2000);
//        }
//    }

    @Test
    public void sendQueryMessage() throws Exception {
        ChatClient chatClient = ChatClient.getSingleInstance();
        chatClient.connectChatServer("localhost", 7040, userId, "xxx", new SimpleMessageHandler());
        Thread.sleep(3000);
        while (true) {
            Message message = Message.getAffairChatMsg(MessageSubType.DEFAULT, affairId, userId, roleId, toUserId, toRoleId, null, null);
            chatClient.messageQuery(10, new Date(), message, new MessageAsyncRequestHandler());
            Thread.sleep(2000);
        }
    }

//    @Test
//    public void unreadMessageCount() throws Exception {
//        ChatClient chatClient = ChatClient.getSingleInstance();
//        chatClient.connectChatServer("localhost", 7040, userId, "xxx", new SimpleMessageHandler());
//        Thread.sleep(3000);
//        while (true) {
//            Message message = Message.getAffairChatMsg(MessageSubType.DEFAULT, affairId, userId, roleId, toUserId, toRoleId, null, null);
//            chatClient.getUnreadMessageCount(message, new MessageAsyncRequestHandler());
//            Thread.sleep(2000);
//        }
//    }
}
