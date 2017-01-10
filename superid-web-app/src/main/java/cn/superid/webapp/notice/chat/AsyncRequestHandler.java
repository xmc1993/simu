package cn.superid.webapp.notice.chat;

/**
 * Created by jessiechen on 10/01/17.
 */
public abstract class AsyncRequestHandler {

    //异步请求返回成功的回调方法
    public abstract void onSuccess();

    //异步请求返回失败的回调方法
    public abstract void onError();
}
