package com.wxprogrem.RabbitMqConsumer;

import com.wxprogrem.config.RabbitMqConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqDelayedCacheConsumer {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitListener(queues = RabbitMqConfiguration.DLX_QUEUE)
    //延时双删
    public void deleteCache(String key) {
        // 延时3秒后，第二次删除 Redis
        redisTemplate.delete(key);
        System.out.println("延时双删完成，清理redis脏缓存" + key);
    }
}
