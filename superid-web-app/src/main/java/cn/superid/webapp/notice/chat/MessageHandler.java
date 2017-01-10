package cn.superid.webapp.notice.chat;

import cn.superid.webapp.notice.chat.proto.Message;

/**
 * Created by jessiechen on 10/01/17.
 */
public abstract class MessageHandler {

    //客户端需要自行重写该方法，用来处理具体的Message
    public abstract void handleMessage(Message message);

    //客户端被踢下线
    public abstract void handleBumped();

    //同一账号有新客户端登录
    public abstract void handleNewLogin(String data);

    //与服务器的物理连接中断
    public abstract void handleDisconnect();

}
