package cn.superid.webapp.notice.chat;

import cn.superid.webapp.notice.chat.proto.Message;

/**
 * Created by jessiechen on 10/01/17.
 */
public class SimpleMessageHandler extends MessageHandler {

    //客户端需要自行重写该方法，用来处理具体的Message
    public void handleMessage(Message message) {
        System.out.println("收到一条新消息" + message.toString());
    }

    //客户端被踢下线
    public void handleBumped() {
        System.out.println("客户端被踢下线");
    }

    //同一账号有新客户端登录
    public void handleNewLogin(String data) {
        System.out.println("有新客户端上线了" + data);
    }

    @Override
    public void handleDisconnect() {
        System.out.println("连接中断");
    }

}
