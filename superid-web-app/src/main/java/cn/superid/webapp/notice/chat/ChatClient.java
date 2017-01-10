package cn.superid.webapp.notice.chat;

import cn.superid.webapp.notice.chat.Constant.C2CType;
import cn.superid.webapp.notice.chat.proto.C2C;
import cn.superid.webapp.notice.tcp.Composer;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessiechen on 10/01/17.
 */
public class ChatClient {
    private static Socket socket;          //连接服务器的socket
    private static InputStream inputStream;   //socket的输入流
    private static OutputStream outputStream;   //socket的输出流
    private final static Codec<C2C> codec = ProtobufProxy.create(C2C.class);  //proto的编码帮助类
    private static char[] possible = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final String AGENT = "Android";
    private static MessageHandler messageHandler;
    private static Composer composer;
    private static int port;
    private static long userId;
    private static String token;
    private static Map<String, AsyncRequestHandler> requestCache = new HashMap<>();
    private static Thread consumeMessageThread;

    //连接到服务器
    private static void connectSocket(String host, int port) throws IOException {
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        System.out.println("连接服务器成功");
        consumeMessageThread = new Thread(new MessageListener());
        consumeMessageThread.start();
        //监听来自服务器的消息，将字节流读到byte数据并发送给Composer处理
    }

    public static void connectChatServer(String host, int port, long userId, String token, MessageHandler messageHandler) throws IOException {
        ChatClient.port = port;
        ChatClient.userId = userId;
        ChatClient.token = token;
        //设置客户端Message处理器
        if (messageHandler != null) ChatClient.messageHandler = messageHandler;
        if (messageHandler == null) throw new RuntimeException("message Handler must not be null");
        //如果Socket没有连接上，就先连接socket
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            connectSocket(host, port);
        }
        //如果composer没有被正确设置，则设置composer
        if (composer == null) {
            initComposer();
        }
        String requestId = generateRequestId(userId);
        ConnectParam connectParam = new ConnectParam(userId, AGENT, token);
        C2C c2c = new C2C(C2CType.SIGN_IN, requestId);
        c2c.setParams(connectParam.toString());
        c2c.setRequestId(requestId);
        outputStream.write(codec.encode(c2c));
        outputStream.flush();
    }

    private static void initComposer() {
        composer = new Composer() {
            @Override
            public void onMessage(C2C c2c) throws IOException {
                 switch (c2c.getType()) {
                    case C2CType.MSG:
                        messageHandler.handleMessage(c2c.getChat());
                        break;
                    case C2CType.BUMPED:
                        messageHandler.handleBumped();
                        break;
                    case C2CType.NEW_LOGIN:
                        messageHandler.handleNewLogin(c2c.getData());
                        break;
                    case C2CType.REDIRECT:
                        connectChatServer("http://" + c2c.getData(), port, userId, token, null);
                        break;
                    case C2CType.RESPONSE:
                        AsyncRequestHandler success = requestCache.get(c2c.getRequestId());
                        if (success != null) {
                            success.onSuccess();
                            requestCache.remove(c2c.getRequestId());
                        }
                        System.out.println(c2c.getType());
                        break;
                    case C2CType.ERROR:
                        AsyncRequestHandler error = requestCache.get(c2c.getRequestId());
                        if (error != null) {
                            error.onError();
                            requestCache.remove(c2c.getRequestId());
                        }
                        System.out.println(c2c.getType());
                        break;
                    case C2CType.PONG:
                        System.out.println("ping success");
                        break;
                    default:
                        System.out.println(c2c.getType());
                }
            }
        };
    }


    private static String generateRequestId(long n) {
        StringBuffer text = new StringBuffer();
        if (n == 0) {
            return "A";
        }
        int a;
        while (n > 0) {
            a = (int) n % possible.length;
            n = n / possible.length;
            text.append(possible[a]);
        }
        return text.toString();
    }

    private static class ConnectParam {
        private long userId;
        private String agent;
        private String token;

        ConnectParam(long userId, String agent, String token) {
            this.userId = userId;
            this.agent = agent;
            this.token = token;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer("{\"userId\":");
            sb.append(userId).append(",\"agent\":\"");
            sb.append(agent).append("\",\"token\":\"").append(token).append("\"}");
            return sb.toString();
        }
    }

    private static class MessageListener implements Runnable {
        @Override
        public void run() {
            System.out.println("消息监听线程开始工作");
            byte[] buff = new byte[4096];
            int k = -1;
            while (true) {
                if (!socket.isClosed() && socket.isConnected() && !socket.isInputShutdown()) {
                    try {
                        if ((k = inputStream.read(buff, 0, buff.length)) != -1) {
                            byte[] resultBuff = new byte[k];
                            System.arraycopy(buff, 0, resultBuff, 0, k); // copy previous bytes
                            System.out.println("message listener thread:" + Arrays.toString(resultBuff));
                            composer.feed(resultBuff);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    messageHandler.handleDisconnect();
                    return; //结束线程
                }
            }
        }
    }

    public static void main(String args[]) throws IOException {
        ChatClient.connectChatServer("localhost", 7040, 1921, "xxx", new SimpleMessageHandler());
    }
}
