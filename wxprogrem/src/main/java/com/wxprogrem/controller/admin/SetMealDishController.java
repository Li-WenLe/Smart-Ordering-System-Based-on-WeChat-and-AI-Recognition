package com.wxprogrem.controller.admin;

import com.wxprogrem.pojo.SetMealDish;
import com.wxprogrem.service.SetMealDishService;
import com.wxprogrem.service.SetMealService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.wxprogrem.constants.Constants.*;

@CrossOrigin
@RestController
@RequestMapping("/setmealdish")
@Slf4j

@Tag(name="修改套餐包含的菜品信息",description = "修改套餐包含的菜品信息相关api")
public class SetMealDishController {
    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Operation(summary = "根据套餐ID和商品列表修改套餐包含的商品信息",description = "根据套餐ID和商品列表修改套餐包含的商品信息")
    @PostMapping("/update")   //修改套餐包含的商品信息
    public Result update(@RequestParam int setmealId,
                         @RequestBody List<SetMealDish> dishes) {
        log.info("修改套餐包含的商品信息--前端传递参数： setmealId：{}，dishes：{}", setmealId, dishes);
        String setMealName=setMealService.getSetMealById(setmealId).getName();
        stringRedisTemplate.delete(SETMEALALL);
        stringRedisTemplate.delete(SETMEALID+setmealId);
        stringRedisTemplate.delete(SETMEALNAME+setMealName);
        RLock lock=redissonClient.getLock(SETMEALLOCK+setmealId);
        try {
            boolean isLocked= lock.tryLock(5,10, TimeUnit.SECONDS);
            if(!isLocked){
                log.error("获取分布式锁失败，setMealId: {}",setmealId);
                throw new RuntimeException("系统繁忙，请稍后重试");
            }
            //删除原套餐的所有菜品
            setMealDishService.delete(setmealId);
            //插入新的数据
            setMealDishService.insert(setmealId,dishes);
        }catch (Exception e){
            log.error("请稍后重试，锁竞争失败，setMealId: {}",setmealId);
            e.printStackTrace();
        }

        return Result.success();
    }

}
