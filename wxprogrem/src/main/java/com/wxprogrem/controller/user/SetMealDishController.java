package com.wxprogrem.controller.user;

import cn.hutool.json.JSONUtil;
import com.wxprogrem.pojo.SetMealDish;
import com.wxprogrem.service.SetMealDishService;
import com.wxprogrem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.wxprogrem.constants.Constants.SETMEALINCLUDEDISHBYSETMEALID;

@Slf4j
@RestController("userSetMealController")
@RequestMapping("/user/setmealdish")
public class SetMealDishController {
    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @PostMapping("/getbysetmealid")
    public Result<List<SetMealDish>> getBySetMealId(@RequestBody SetMealDish setMealDish) {
        log.info("前端传递的参数：{}", setMealDish);
        int setMealId = setMealDish.getSetmealId();
        String setMealDishListJson = stringRedisTemplate.opsForValue().get(SETMEALINCLUDEDISHBYSETMEALID+setMealId);
        if(setMealDishListJson != null&&!setMealDishListJson.isEmpty()){
            List<SetMealDish>redisStoreSetMealList= JSONUtil.toList(setMealDishListJson, SetMealDish.class);
            return Result.success(redisStoreSetMealList);
        }
        List<SetMealDish> setMealDishList=setMealDishService.getBySetMealId(setMealId);
        stringRedisTemplate.opsForValue().set(SETMEALINCLUDEDISHBYSETMEALID+setMealId,JSONUtil.toJsonStr(setMealDishList));
        return Result.success(setMealDishList);
    }
}
