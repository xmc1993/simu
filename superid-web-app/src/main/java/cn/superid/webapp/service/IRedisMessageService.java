package cn.superid.webapp.service;

import cn.superid.webapp.forms.Message;

/**
 * Created by xmc1993 on 16/9/12.
 */
public interface IRedisMessageService {
    /**
     * 向redis指定频道发布消息
     * @param channel
     * @param message
     */
    void sendJsonMessage(String channel, Message message);
}
