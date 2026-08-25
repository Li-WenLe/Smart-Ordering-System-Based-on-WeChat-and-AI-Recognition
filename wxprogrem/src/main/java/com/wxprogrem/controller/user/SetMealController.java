package com.wxprogrem.controller.user;

import cn.hutool.json.JSONUtil;
import com.wxprogrem.pojo.SetMeal;
import com.wxprogrem.service.SetMealService;
import com.wxprogrem.service.UserService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController("/userSetMealController")
@RequestMapping("/user/setmeal")
@Tag(name = "用户端套餐相关接口",description = "用户端优惠券秒杀相关api")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "获取所有的套餐信息",description = "获取所有的套餐信息")
    @PostMapping("/all")
    public Result<List<SetMeal>> getSetMealService() {
        String key="setmeal_all";
        String SetMeallistJson=stringRedisTemplate.opsForValue().get(key);
        if(SetMeallistJson!=null&&SetMeallistJson.length()>0){
            List<SetMeal>list= JSONUtil.toList(SetMeallistJson, SetMeal.class);
            return Result.success(list);
        }
        List<SetMeal>list=setMealService.getAllSetMeal();
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(list));
        return Result.success(list);
    }
    @Operation(summary = "根据套餐名获取套餐信息",description = "根据套餐名获取套餐信息")
    @PostMapping
    public Result<SetMeal> getSetMealByName(@RequestBody Map<String,String> map) {
        String name=map.get("name");
        log.info("前端传递参数：{}",name);
        String key="setmeal_"+name;
        String setMealJson=stringRedisTemplate.opsForValue().get(key);
        if(setMealJson!=null){
            SetMeal setMeal=JSONUtil.toBean(setMealJson, SetMeal.class);
            return Result.success(setMeal);
        }
        SetMeal setMeal=setMealService.getSetMealByName(name);
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(setMeal));
        return Result.success(setMeal);
    }

    @Operation(summary = "根据套餐ID获取套餐信息",description = "根据套餐ID获取套餐信息")
    @PostMapping("/getbyid")
    public Result<SetMeal> getSetMealById(@RequestBody Map<String,String> map){
        log.info("id:{}",map.get("id"));
        int id=Integer.parseInt(map.get("id"));
        String key="setmeal_"+id;
        String setMealJson=stringRedisTemplate.opsForValue().get(key);
        if(setMealJson!=null){
            SetMeal setMeal=JSONUtil.toBean(setMealJson, SetMeal.class);
            return Result.success(setMeal);
        }
        SetMeal setMeal=setMealService.getSetMealById(id);
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(setMeal));
        return Result.success(setMeal);
    }
}
