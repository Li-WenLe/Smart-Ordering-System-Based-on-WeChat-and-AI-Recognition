package com.wxprogrem.controller.user;

import cn.hutool.json.JSONUtil;
import com.wxprogrem.config.RabbitMqConfiguration;
import com.wxprogrem.constants.RedisData;
import com.wxprogrem.pojo.DishType;
import com.wxprogrem.service.DishTypeService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.wxprogrem.constants.Constants.DISHTYPEONSHOW;
import static com.wxprogrem.constants.Constants.DISHTYPEONSHOWLOCK;

@Slf4j
@CrossOrigin
@RestController("/user")
@RequestMapping("/user/dishtype")
@Tag(name="用户端菜品分类相关接口",description = "用户端菜品分类接口相关api")
public class DishTypeController {
    @Autowired
    private DishTypeService dishTypeService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Operation(summary = "获取处于OnShow状态下的菜品分类",description = "获取处于OnShow状态下的菜品分类")
    @GetMapping
    public Result<List<DishType>> getTypeOnshow(){
        String dishTypeOnShowString = redisTemplate.opsForValue().get(DISHTYPEONSHOW);
        RedisData dishTypeOnShow= JSONUtil.toBean(dishTypeOnShowString, RedisData.class);
        if(dishTypeOnShow.getData() != null&& !dishTypeOnShow.getData().equals("")){
            List<DishType>dishTypeOnShowList= (List<DishType>) dishTypeOnShow.getData();
            //如果过期，加入消费队列异步更新,发送消息到事务队列
            if(dishTypeOnShow.getExpireTime().isBefore(LocalDateTime.now())){
                //尝试获取锁
                RLock lock = redissonClient.getLock(DISHTYPEONSHOWLOCK );
                try {
                    if(lock.tryLock(1,5, TimeUnit.SECONDS)){
                        rabbitTemplate.convertAndSend(RabbitMqConfiguration.BUSINESS_QUEUE,dishTypeOnShowString);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    lock.unlock();
                }
            }
            return Result.success(dishTypeOnShowList);
        }
        //双重保险，缓存没有就再查再存
        List<DishType> list=dishTypeService.getDishtypeOnshow();
        RedisData redisData=new RedisData();
        redisData.setData(list);
        redisData.setExpireTime(LocalDateTime.now().plusMinutes(30));
        redisTemplate.opsForValue().set(DISHTYPEONSHOW,JSONUtil.toJsonStr(redisData));
        return Result.success(list);
    }

    @Operation(summary = "获取所有的菜品分类",description = "获取所有的菜品分类")
    @PostMapping("/all")
    public Result<List<DishType>> getTypeAll(){
        List<DishType>list=dishTypeService.getDishTypeList();
        return Result.success(list);
    }

}
