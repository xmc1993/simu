package cn.superid.webapp.service.impl;

import cn.superid.webapp.service.IRedisMessageService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void sendMessage(String channel, Serializable message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
