package cn.superid.webapp.service.impl;

import cn.superid.jpa.redis.RedisUtil;
import cn.superid.webapp.forms.Message;
import cn.superid.webapp.service.IRedisMessageService;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xmc1993 on 16/9/12.
 */
@Service
public class RedisMessageService implements IRedisMessageService {

    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);


    @Override
    public void sendJsonMessage(final String channel, Message message) {
        Gson gson = new Gson();
        final String msg = gson.toJson(message);

        executor.submit(new Runnable() {
            @Override
            public void run() {
                RedisUtil.getJedis().publish(channel.getBytes(), msg.getBytes());
            }
        });
    }
}
