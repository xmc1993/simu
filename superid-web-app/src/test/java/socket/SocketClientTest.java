package socket;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by xmc1993 on 16/10/27.
 */
public class SocketClientTest {
    Socket socket;

    @Test
    public void startConnect() throws IOException {
        socket = new Socket("localhost", 9999);


//        bw.close();
//        socket.close();

        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        Message.NoticeMsg.Builder noticeMsg = Message.NoticeMsg.newBuilder();
//                        noticeMsg.setType("测试");
//                        noticeMsg.setContent("i am ");
//                        noticeMsg.setAffairId(12345L);
//                        noticeMsg.setDisplayName("xmc1993");
//                        Message.NoticeMsg notice = noticeMsg.build();
//                        notice.writeTo(socket.getOutputStream());
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        for (int i = 0; i < 10000; i++) {
                            bw.write(i + "hello world \n");
                            bw.flush();
                        }
//                        socket.getOutputStream().write("hello world\n".getBytes());
//                        socket.getOutputStream().flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }


}
