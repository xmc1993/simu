package cn.superid.webapp.service.impl;

import cn.superid.webapp.forms.Message;
import cn.superid.webapp.service.IRedisMessageService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by xmc1993 on 16/9/12.
 */
@Service
public class RedisMessageService implements IRedisMessageService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Override
    public void sendMessage(String channel, Serializable message) {
        redisTemplate.convertAndSend(channel, message);
    }

    @Override
    public void sendJsonMessage(String channel, Message message) {
        Gson gson = new Gson();
        String msg = gson.toJson(message);
        //TODO 改为线程池的方式?
        jedisConnectionFactory.getConnection().publish("room_message".getBytes(), msg.getBytes());

    }
}
