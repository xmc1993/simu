package cn.superid.webapp.service;

import cn.superid.webapp.forms.Message;

import java.io.Serializable;

/**
 * Created by xmc1993 on 16/9/12.
 */
public interface IRedisMessageService {

    /**
     *
     * @param channel
     * @param message
     */
    void sendMessage(String channel, Serializable message);


    void sendJsonMessage(String channel, Message message);
}
