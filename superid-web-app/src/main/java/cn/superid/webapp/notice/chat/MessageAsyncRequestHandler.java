package cn.superid.webapp.notice.chat;

import cn.superid.webapp.notice.chat.proto.C2C;

/**
 * Created by jessiechen on 10/01/17.
 */
public class MessageAsyncRequestHandler extends AsyncRequestHandler {

    //消息发送成功
    public void onSuccess(C2C c2c) {
        System.out.println("消息发送成功：" + c2c.toString());
    }

    //异步请求返回失败的回调方法
    public void onError(C2C c2c) {
        System.out.println("消息发送失败：" + c2c.toString());
    }
}
