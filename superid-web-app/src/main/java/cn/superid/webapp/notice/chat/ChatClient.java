package cn.superid.webapp.notice.chat;

import cn.superid.webapp.notice.chat.Constant.C2CType;
import cn.superid.webapp.notice.chat.exception.NotLoginException;
import cn.superid.webapp.notice.chat.proto.C2C;
import cn.superid.webapp.notice.chat.proto.Message;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
    private String host;
    private String token;
    private boolean loginSuccess;
    private Map<String, AsyncRequestHandler> requestCache = new HashMap<>();//有返回结果的request的缓存池
    private Thread consumeMessageThread;   //监听inputstream的的线程
    private Thread pingThread;   //监听inputstream的的线程
    private boolean consumeMessageThreadSignal;
    private boolean pingThreadSignal;
    private int failPingCount =0;
    private boolean pingSuccess = false;
    private int pingInterval = 3*60*1000;
    private int pingTimeOut = 5000;
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
        if(socket!=null&&socket.isConnected()){
            socket.close();
            inputStream.close();
            outputStream.close();
        }
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        System.out.println("连接服务器成功"+host+":"+port);
        //监听来自服务器的消息，将字节流读到byte数据并发送给Composer处理
    }

    //连接并登陆到服务器
    public void connectChatServer(String ip, long userId, String token, MessageHandler messageHandler) throws IOException {
        String[] tmp = ip.split(":");
        this.host = tmp[0];
        this.port = Integer.parseInt(tmp[1])-1;
        this.userId = userId;
        this.token = token;
        //设置客户端Message处理器
        if (messageHandler != null) this.messageHandler = messageHandler;
        if (messageHandler == null) throw new RuntimeException("message Handler must not be null");
//        //如果Socket没有连接上，就先连接socket
//        if (socket == null || socket.isClosed() || !socket.isConnected()) {
//        }
        connectSocket(host, port);
        //设置消息监听线程
        consumeMessageThread = new Thread(new MessageListenerRunnable());
        consumeMessageThreadSignal = true;
        consumeMessageThread.start();



        //如果composer没有被正确设置，则设置composer
        if (composer == null) {
            initComposer();
        }
        //登陆到服务器
        loginServer(userId, token);

        //设置心跳检测线程
        pingThread = new Thread(new PingRunnable());
        pingThreadSignal = true;
        pingThread.start();

    }

    //关闭服务器的连接
    public void closeConnect() throws IOException {
        //关闭线程
        consumeMessageThreadSignal = false;
        pingThreadSignal = false;
        if(inputStream!=null){
            inputStream.close();
            outputStream.close();
            socket.close();
            socket = null;
        }
    }

    //发送普通消息
    public void sendMessage(Message message, AsyncRequestHandler messageAsyncHandler) throws Exception {
        if(socket==null||socket.isClosed()){
            return;
        }
        checkLogin();
        String requestId = generateRequestId(userId);
        C2C c2c = new C2C(C2CType.MSG, requestId);
        c2c.setChat(message);
        outputStream.write(codec.encode(c2c));
        outputStream.flush();
        requestCache.put(requestId, messageAsyncHandler);
    }

    //历史消息查询
    public void messageQuery(int limit, Date endTime, Message message, AsyncRequestHandler messageAsyncHandler) throws Exception {
        checkLogin();
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
        checkLogin();
        String requestId = generateRequestId(userId);
        C2C c2c = new C2C(C2CType.GET_UNREAD_COUNT, requestId);
        c2c.setChat(message);
        outputStream.write(codec.encode(c2c));
        outputStream.flush();
        requestCache.put(requestId, messageAsyncHandler);
    }

    private void checkLogin() throws NotLoginException {
        if (!loginSuccess) throw new NotLoginException();
    }

    private void loginServer(long userId, String token) throws IOException {
        loginSuccess=true;
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
                try {
                    System.out.println(c2c.getData());
                    Thread.sleep(5000);//先观望一些,让子弹飞一会儿
//                    closeConnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                messageHandler.loginFail();
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
                        connectChatServer(c2c.getData(),  userId, token, messageHandler);
                        break;
                    case C2CType.RESPONSE:
                        AsyncRequestHandler success = requestCache.get(c2c.getRequestId());
                        if (success != null) {
                            success.onSuccess(c2c);
                            requestCache.remove(c2c.getRequestId());
                        }
                        break;
                    case C2CType.ERROR:
                        System.out.println("onMessage error" + c2c.getData());
                        AsyncRequestHandler error = requestCache.get(c2c.getRequestId());
                        if (error != null) {
                            error.onError(c2c);
                            requestCache.remove(c2c.getRequestId());
                        }
                        break;
                    case C2CType.PONG:
                        pingSuccess = true;
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

    private class MessageListenerRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("消息监听线程开始工作");
            byte[] buff = new byte[4096];
            int k = -1;
            while (consumeMessageThreadSignal) {
                if (!socket.isClosed() && socket.isConnected() && !socket.isInputShutdown()) {//不在这里判断连接是否中断，socket不可靠
                    try {
                        if ((k = inputStream.read(buff, 0, buff.length)) != -1) {//当在close方法里关闭了inputStream,这里就会报Exception，而consumeMessageThreadSignal又被关闭，所以线程会退出
                            byte[] resultBuff = new byte[k];
                            System.arraycopy(buff, 0, resultBuff, 0, k); // copy previous bytes
                           // System.out.println("message listener thread:" + Arrays.toString(resultBuff));
                            composer.feed(resultBuff);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    private class PingRunnable implements Runnable {
        @Override
        public void run() {
            C2C ping = new C2C(C2CType.PING, null);
            while (pingThreadSignal) {
                try {
                    Thread.sleep(pingInterval);
                    if(socket!=null&&socket.isConnected()){
                        outputStream.write(codec.encode(ping));
                        outputStream.flush();
                        pingSuccess = false;
                        Thread.sleep(pingTimeOut);
                        if (!pingSuccess) {
                            failPingCount++;
                            if(failPingCount==3){
                                closeConnect();
                                messageHandler.handleDisconnect();
                            }
                        } else {
                            System.out.println("ping 成功！！！");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
