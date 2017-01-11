package cn.superid.webapp.notice.chat;

import cn.superid.webapp.notice.chat.Constant.C2CType;
import cn.superid.webapp.notice.chat.Constant.MessageSubType;
import cn.superid.webapp.notice.chat.Constant.MessageType;
import cn.superid.webapp.notice.chat.proto.C2C;
import cn.superid.webapp.notice.chat.proto.Message;
import cn.superid.webapp.notice.tcp.Composer;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessiechen on 10/01/17.
 */
public class ChatClient {
    private static Socket socket;          //连接服务器的socket
    private static InputStream inputStream;   //socket的输入流
    private static OutputStream outputStream;   //socket的输出流
    private Codec<C2C> codec;  //proto的编码帮助类
    private static char[] possible = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};//生成requestId的取值范围
    private final String AGENT = "Android";
    private MessageHandler messageHandler; //接受消息处理器
    private Composer composer;//原始byte流处理类
    private int port;
    private long userId;
    private String token;
    private boolean loginSuccess;
    private Map<String, AsyncRequestHandler> requestCache = new HashMap<>();//有返回结果的request的缓存池
    private Thread consumeMessageThread;   //监听inputstream的的线程
    private static ChatClient singleInstance;

    private ChatClient() {
        codec = ProtobufProxy.create(C2C.class);
    }

    public static ChatClient getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new ChatClient();
        }
        return singleInstance;
    }

    //连接到服务器，物理连接
    private void connectSocket(String host, int port) throws IOException {
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        System.out.println("连接服务器成功");
        consumeMessageThread = new Thread(new MessageListener());
        consumeMessageThread.start();
        //监听来自服务器的消息，将字节流读到byte数据并发送给Composer处理
    }

    //连接并登陆到服务器
    public void connectChatServer(String host, int port, long userId, String token, MessageHandler messageHandler) throws IOException {
        this.port = port;
        this.userId = userId;
        this.token = token;
        //设置客户端Message处理器
        if (messageHandler != null) this.messageHandler = messageHandler;
        if (messageHandler == null) throw new RuntimeException("message Handler must not be null");
        //如果Socket没有连接上，就先连接socket
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            connectSocket(host, port);
        }
        //如果composer没有被正确设置，则设置composer
        if (composer == null) {
            initComposer();
        }
        //登陆到服务器
        loginServer(userId, token);
    }

    //发送普通消息
    public void sendMessage(Message message, AsyncRequestHandler messageAsyncHandler) throws Exception {
        String requestId = generateRequestId(userId);
        C2C c2c = new C2C(C2CType.MSG, requestId);
        c2c.setChat(message);
        outputStream.write(codec.encode(c2c));
        outputStream.flush();
        requestCache.put(requestId, messageAsyncHandler);
    }

    //历史消息查询
    public void messageQuery(int limit, Date endTime, Message message, AsyncRequestHandler messageAsyncHandler) throws Exception {
        String requestId = generateRequestId(userId);
        MessageQwuerytParam param = new MessageQwuerytParam(limit, endTime.getTime());
        C2C c2c = new C2C(C2CType.ROOM_MSG_QUERY, requestId);
        c2c.setParams(param.toString());
        c2c.setChat(message);
        outputStream.write(codec.encode(c2c));
        outputStream.flush();
        requestCache.put(requestId, messageAsyncHandler);
    }

    //获取某窗口内的未读消息个数
    public void getUnreadMessageCount(Message message, AsyncRequestHandler messageAsyncHandler) throws Exception {
        String requestId = generateRequestId(userId);
        C2C c2c = new C2C(C2CType.GET_UNREAD_COUNT, requestId);
        c2c.setChat(message);
        outputStream.write(codec.encode(c2c));
        outputStream.flush();
        requestCache.put(requestId, messageAsyncHandler);
    }

    private void loginServer(long userId, String token) throws IOException {
        String requestId = generateRequestId(userId);
        ConnectParam connectParam = new ConnectParam(userId, AGENT, token);
        C2C c2c = new C2C(C2CType.SIGN_IN, requestId);
        c2c.setParams(connectParam.toString());
        c2c.setRequestId(requestId);
        outputStream.write(codec.encode(c2c));
        outputStream.flush();
        AsyncRequestHandler loginHandler = new AsyncRequestHandler() {
            @Override
            public void onSuccess(C2C c2c) {
                System.out.println("登陆成功！！！");
                System.out.println(c2c);
                loginSuccess = true;
            }

            @Override
            public void onError(C2C c2c) {
                System.out.println("登陆失败！！！");
                System.out.println(c2c);
                loginSuccess = false;
            }
        };
        requestCache.put(requestId, loginHandler);
    }

    private void initComposer() {
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
                        connectChatServer(c2c.getData(), port, userId, token, null);
                        break;
                    case C2CType.RESPONSE:
                        System.out.println("onMessage success" + c2c.getType());
                        AsyncRequestHandler success = requestCache.get(c2c.getRequestId());
                        if (success != null) {
                            success.onSuccess(c2c);
                            requestCache.remove(c2c.getRequestId());
                        }
                        break;
                    case C2CType.ERROR:
                        System.out.println("onMessage error" + c2c.getType());
                        AsyncRequestHandler error = requestCache.get(c2c.getRequestId());
                        if (error != null) {
                            error.onError(c2c);
                            requestCache.remove(c2c.getRequestId());
                        }
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

    //requestId生成器
    private static String generateRequestId(long userId) {
        return longToString(userId) + "$" + longToString(new Date().getTime());
    }

    private static String longToString(long n) {
        StringBuffer text = new StringBuffer();
        if (n == 0) {
            return "A";
        }
        int a;
        while (n > 0) {
            a = new Long(n % possible.length).intValue();
            n = n / possible.length;
            text.append(possible[a]);
        }
        return text.toString();
    }

    //登陆时的ConnectParam
    private class ConnectParam {
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

    private class MessageQwuerytParam {
        private int limit;
        private long endTimeMillis;

        MessageQwuerytParam(int limit, long endTimeMillis) {
            this.limit = limit;
            this.endTimeMillis = endTimeMillis;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer("{\"limit\":");
            sb.append(limit).append(",\"endTime\":\"");
            sb.append(endTimeMillis).append("\"}");
            return sb.toString();
        }
    }

    private class MessageListener implements Runnable {
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

    public static void main(String args[]) throws Exception {
        ChatClient chatClient = ChatClient.getSingleInstance();
        long userId = 1912l;
        long roleId = 3676l;
        long toUserId = 1911l;
        long toRoleId = 3675l;
        long affairId = 7620l;
        chatClient.connectChatServer("localhost", 7040, userId, "xxx", new SimpleMessageHandler());
        int counter = 1;
    }
}
