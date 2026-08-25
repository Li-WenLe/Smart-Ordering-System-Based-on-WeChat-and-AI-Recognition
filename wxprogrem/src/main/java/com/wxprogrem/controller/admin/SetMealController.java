package com.wxprogrem.controller.admin;

import cn.hutool.json.JSONUtil;
import com.wxprogrem.pojo.Dish;
import com.wxprogrem.pojo.SetMeal;
import com.wxprogrem.pojo.SetMealDish;
import com.wxprogrem.service.CacheDelateService;
import com.wxprogrem.service.SetMealDishService;
import com.wxprogrem.service.SetMealService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wxprogrem.config.RabbitMqConfiguration.DELAYED_BUSINESS_EXCHANGE;
import static com.wxprogrem.config.RabbitMqConfiguration.DELAYED_BUSINESS_QUEUE;
import static com.wxprogrem.constants.Constants.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/setmeal")

@Tag(name="商家管理端套餐管理",description = "商家管理端套餐管理")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CacheDelateService cacheDelateService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Qualifier("DelayedBusinessBinding")
    @Autowired
    private Binding delayedBusinessBinding;

    @Operation(summary = "获取所有套餐信息",description = "获取所有套餐信息")
    @GetMapping                                     //获取所有套餐信息
    public Result<List<SetMeal>>getAllSetMeal() {
        log.info("获取所有套餐信息***************");
        String allSetmealList=stringRedisTemplate.opsForValue().get(SETMEALALL);
        if(allSetmealList!=null&&!allSetmealList.isEmpty()){
            List<SetMeal>list= JSONUtil.toList(allSetmealList, SetMeal.class);
            return Result.success(list);
        }
        List<SetMeal>list=setMealService.getAllSetMeal();
        stringRedisTemplate.opsForValue().set(SETMEALALL,JSONUtil.toJsonStr(list));
        return Result.success(list);
    }


    @Operation(summary = "添加套餐信息",description = "添加套餐信息")
    @PostMapping("/add")                           //添加套餐信息
    public Result addSetMeal(@RequestBody SetMeal setMeal) {
        log.info("添加套餐信息--前端传递套餐参数信息：{}",setMeal);
        stringRedisTemplate.delete(SETMEALALL);
        setMealService.addSetMeal(setMeal);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,SETMEALALL);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,SETMEALALL);
        return Result.success();
    }

    @Operation(summary = "修改套餐信息",description = "修改套餐信息")
    @PutMapping                                       //修改套餐信息
    public Result updateSetMeal(@RequestBody SetMeal setMeal) {
        log.info("修改套餐信息--前端传递的套餐参数信息参数：setMeal:{}",setMeal);
        int id=setMeal.getId();
        String name=setMeal.getName();
        stringRedisTemplate.delete(SETMEALID+id);
        stringRedisTemplate.delete(SETMEALNAME+name);
        stringRedisTemplate.delete(SETMEALALL);
        setMealService.updateSetMeal(setMeal);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,SETMEALALL);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,SETMEALALL);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,SETMEALID+id);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,SETMEALID+id);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,SETMEALNAME+name);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,SETMEALNAME+name);
        return Result.success();
    }


    @Operation(summary = "根据套餐ID获取套餐信息",description = "根据套餐ID获取套餐信息")
    @PostMapping("/getbyid")                          //根据id获取套餐信息
    public Result getSetMealById(int id) {
        log.info("根据id获取套餐信息--前端传递的id参数：id:{}",id);
        String setMealJson=stringRedisTemplate.opsForValue().get(SETMEALID+id);
        if(setMealJson!=null&&!setMealJson.isEmpty()){
            SetMeal setMeal=JSONUtil.toBean(setMealJson, SetMeal.class);
            return Result.success(setMeal);
        }
        SetMeal setMeal=setMealService.getSetMealById(id);
        stringRedisTemplate.opsForValue().set(SETMEALID+id,JSONUtil.toJsonStr(setMeal));
        return Result.success(setMeal);
    }


    @Operation(summary = "根据套餐ID获取套餐包含的菜品信息",description = "根据套餐ID获取套餐包含的菜品信息")                                                      //根据id获取套餐包含的商品信息
    @PostMapping("/getdish")
    public Result<List<SetMealDish>> getSetMealDish(int id) {
        log.info("根据id获取套餐包含的商品信息--前端传递的套餐id:{}",id);
        String SetMealListJson =stringRedisTemplate.opsForValue().get(SETMEALINCLUDEDISHBYSETMEALID+id);
        if(SetMealListJson!=null&&!SetMealListJson.isEmpty()){
            List<SetMealDish>redisStoreSetMealList=JSONUtil.toList(SetMealListJson, SetMealDish.class);
            return Result.success(redisStoreSetMealList);
        }
        List<SetMealDish>list=setMealDishService.getSetMealDishBySetMealId(id);
        stringRedisTemplate.opsForValue().set(SETMEALINCLUDEDISHBYSETMEALID+id,JSONUtil.toJsonStr(list));
        return Result.success(list);
    }


    @Operation(summary = "根据套餐ID修改套餐包含的菜品信息",description = "根据套餐ID修改套餐包含的菜品信息")
    @PostMapping("/update")                               //根据id修改套餐信息
    public Result updateSetMealDish(@RequestBody SetMeal setMeal) {
        log.info("修改套餐信息--前端传递的套餐参数信息参数：setMeal:{}",setMeal);
        int id=setMeal.getId();
        String name=setMeal.getName();
        stringRedisTemplate.delete(SETMEALID+id);
        stringRedisTemplate.delete(SETMEALNAME+name);
        stringRedisTemplate.delete(SETMEALALL);
        setMealService.updateSetMeal(setMeal);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,SETMEALALL);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,SETMEALALL);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,SETMEALID+id);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,SETMEALID+id);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,SETMEALNAME+name);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,SETMEALNAME+name);
        return Result.success();
    }


    @Operation(summary = "根据套餐ID获取套餐包含的菜品信息",description = "根据套餐ID获取套餐包含的菜品信息")
    @PostMapping("/delete")                                //根据id删除套餐信息
    public Result delete(@RequestParam int setmealId) {
        log.info("根据id删除套餐信息---删除的套餐id：{}",setmealId);
        //删除套餐缓存
        stringRedisTemplate.delete(SETMEALID+setmealId);
        stringRedisTemplate.delete(SETMEALALL);
        setMealService.delete(setmealId);
        //cacheDelateService.deleteCacheAsync(key,key2);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,SETMEALALL);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,SETMEALALL);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,SETMEALID+setmealId);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,SETMEALID+setmealId);
        return Result.success();
    }
}
