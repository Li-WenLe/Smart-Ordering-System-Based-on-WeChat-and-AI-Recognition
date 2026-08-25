package com.wxprogrem.RabbitMqConsumer;

import cn.hutool.json.JSONUtil;
import com.wxprogrem.config.RabbitMqConfiguration;
import com.wxprogrem.constants.RedisData;
import com.wxprogrem.pojo.DishType;
import com.wxprogrem.pojo.OrderDetail;
import com.wxprogrem.service.DishService;
import com.wxprogrem.service.DishTypeService;
import com.wxprogrem.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.wxprogrem.constants.Constants.DISHTYPEONSHOW;

@Component
@Slf4j
public class RabbitMqBusinessConsumer {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    @Autowired
//    private  DishTypeService dishTypeService;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    List<DishType> list=dishTypeService.getDishtypeOnshow();
//    @RabbitListener(queues = RabbitMqConfiguration.BUSINESS_QUEUE)
//    public void process(String dishTypeOnShowString) {
//        //更新数据
//        List<DishType> list=dishTypeService.getDishtypeOnshow();
//        RedisData redisData=new RedisData();
//        redisData.setData(list);
//        //更新逻辑时间
//        redisData.setExpireTime(LocalDateTime.now().plusMinutes(30));
//        stringRedisTemplate.opsForValue().set(DISHTYPEONSHOW, JSONUtil.toJsonStr(redisData));
//    }
@Autowired
private RabbitTemplate rabbitTemplate;
    @Autowired
    private DishTypeService dishTypeService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DishService dishService;





    // Bean全部依赖注入完成后再执行初始化加载
    @PostConstruct
    public void initLoadDishType() {
        List<DishType> list = dishTypeService.getDishtypeOnshow();
        RedisData redisData = new RedisData();
        redisData.setData(list);
        redisData.setExpireTime(LocalDateTime.now().plusMinutes(30));
        stringRedisTemplate.opsForValue().set(DISHTYPEONSHOW, JSONUtil.toJsonStr(redisData));
    }

    @RabbitListener(queues = RabbitMqConfiguration.BUSINESS_QUEUE)
    public void process(String dishTypeOnShowString) {
        // 接收MQ消息后刷新缓存
        List<DishType> list = dishTypeService.getDishtypeOnshow();
        RedisData redisData = new RedisData();
        redisData.setData(list);
        redisData.setExpireTime(LocalDateTime.now().plusMinutes(30));
        stringRedisTemplate.opsForValue().set(DISHTYPEONSHOW, JSONUtil.toJsonStr(redisData));
    }
}
