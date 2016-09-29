package cn.superid.webapp.service.impl;

import cn.superid.webapp.forms.Message;
import cn.superid.webapp.service.IRedisMessageService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xmc1993 on 16/9/12.
 */
@Service
public class RedisMessageService implements IRedisMessageService {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//    @Autowired
//    private JedisConnectionFactory jedisConnectionFactory;
//
//    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Override
    public void sendMessage(String channel, Serializable message) {
//        redisTemplate.convertAndSend(channel, message);
    }

    @Override
    public void sendJsonMessage(String channel, Message message) {
//        Gson gson = new Gson();
//        final String msg = gson.toJson(message);
//
//        executor.submit(new Runnable() {
//            @Override
//            public void run() {
//                jedisConnectionFactory.getConnection().publish("room_message".getBytes(), msg.getBytes());
//            }
//        });
    }
}
