package cn.superid.webapp.notice.chat;

import cn.superid.webapp.notice.chat.proto.C2C;

/**
 * Created by jessiechen on 10/01/17.
 */
public abstract class AsyncRequestHandler {

    //异步请求返回成功的回调方法
    public abstract void onSuccess(C2C c2c);

    //异步请求返回失败的回调方法
    public abstract void onError(C2C c2c);
}
