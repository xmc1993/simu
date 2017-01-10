package socket;

import cn.superid.webapp.notice.chat.ChatClient;
import cn.superid.webapp.notice.chat.SimpleMessageHandler;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jessiechen on 10/01/17.
 */
public class ChatClientTest {
    private long userId = 1912;
    private long roleId = 3676;
    private long toUserId = 1911;
    private long toRoleId = 3675;
    private String host = "localhost";
    private int port = 10110;

    @Test
    public void connectServer() {
        try {
            ChatClient.connectChatServer(host, port, userId, "xxx", new SimpleMessageHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
