package com.wxprogrem.controller.user;

import cn.hutool.json.JSONUtil;
import com.wxprogrem.pojo.Dish;
import com.wxprogrem.service.DishService;
import com.wxprogrem.service.DishTypeService;
//import com.wxprogrem.utils.BloomFilterUtil;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.wxprogrem.constants.Constants.*;

@Slf4j
@CrossOrigin
@RestController("user")
@RequestMapping("/user/dish")

@Tag(name="用户端菜品接口",description = "用户端菜品相关api")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishTypeService dishTypeService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RBloomFilter<String> bloomFilter;


    @Operation(summary="根据分类名获取对应分类下的所有商品信息",description = "根据分类名获取对应分类下的所有商品信息")
    @PostMapping("/getdishbytypename")
    public Result<List<Dish>> getDishByTypeName(@RequestBody Map<String,String> map){
        log.info("根据分类名获取对应分类下的所有商品信息--小程序传递参数：分类名：dishTypeName{}",map.get("name"));
        String name = map.get("name");
        int dishTypeId = dishTypeService.getIdByType(name);
        String dishTypeList = stringRedisTemplate.opsForValue().get(DISHTYPEID+dishTypeId);

        if(dishTypeList != null && !dishTypeList.isEmpty()){
            List<Dish> list = JSONUtil.toList(dishTypeList, Dish.class);
            return Result.success(list);
        }

        List<Dish> list = dishService.getDishByTypeId(dishTypeId);
        log.info("redis存储商品分类信息");
        stringRedisTemplate.opsForValue().set(DISHTYPEID+dishTypeId, JSONUtil.toJsonStr(list));
        return Result.success(list);
    }


    @Operation(summary="根据菜品ID获取菜品信息",description = "根据菜品ID获取菜品信息")
    @GetMapping("/getbyid")
    public Result<Dish> getDishById(@RequestParam String dishId){
       log.info("根据菜品ID获取菜品信息--小程序传递参数：dishId:{}",dishId);
        //布隆过滤器检查
        if (!bloomFilter.contains(String.valueOf(dishId))) {
            log.info("布隆过滤器拦截无效菜品ID查询: {}", dishId);
            return Result.success(null);
        }
        String dishJson = stringRedisTemplate.opsForValue().get(DISHID+dishId);
        if (dishJson != null && !dishJson.isEmpty()) {
            Dish dish = JSONUtil.toBean(dishJson, Dish.class);
            return Result.success(dish);
        }
        Dish dish = dishService.getDishById(Integer.parseInt(dishId));
        if (dish != null) {
            log.info("redis存储单个商品信息");
            stringRedisTemplate.opsForValue().set(DISHID+dishId, JSONUtil.toJsonStr(dish));
        }
        return Result.success(dish);
    }


    @Operation(summary="获取所有处于启动售卖的菜品信息",description = "获取所有处于启动售卖的菜品信息")
    @GetMapping("/onshow")
    public Result<List<Dish>> onShow(){
        log.info("获取所有处于启动售卖的菜品信息*********");
        String onshowDishList = stringRedisTemplate.opsForValue().get(DISHONSHOW);
        if (onshowDishList != null && !onshowDishList.isEmpty()) {
            List<Dish> list = JSONUtil.toList(onshowDishList, Dish.class);
            return Result.success(list);
        }

        List<Dish> list = dishService.getDishByIshow();
        log.info("redis存储首页商品展示信息");
        stringRedisTemplate.opsForValue().set(DISHONSHOW, JSONUtil.toJsonStr(list));
        return Result.success(list);
    }


    @Operation(summary="根据菜品名称获取菜品相关信息",description = "根据菜品名称获取菜品相关信息")
    @PostMapping("/getdishbydishname")
    public Result<Dish> getDishByDishname(@RequestBody Map<String,String> map){
        log.info("根据菜品名称获取菜品相关信息--小程序传递参数：dishName:{}",map.get("name"));
        String name = map.get("name");
        int dishId = dishService.getDishByname(name).getId();
        // 布隆过滤器检查
        if (!bloomFilter.contains(String.valueOf(dishId))) {
            log.info("布隆过滤器拦截无效菜品ID查询: {}", dishId);
            return Result.success(null);
        }
        String dish = stringRedisTemplate.opsForValue().get(DISHID+dishId);
        if(dish != null && !dish.isEmpty()){
            Dish list = JSONUtil.toBean(dish, Dish.class);
            return Result.success(list);
        }
        Dish list = dishService.getDishById(dishId);
        log.info("redis存储商品分类信息");
        stringRedisTemplate.opsForValue().set(DISHID+dishId, JSONUtil.toJsonStr(list));
        return Result.success(list);
    }


    @Operation(summary="根据菜品名称获取菜品相关信息",description = "根据菜品名称获取菜品相关信息")
    @PostMapping("/getdishbyname")
    public Result<Dish> getDishByName(@RequestBody Map<String,String> map){
        log.info("根据菜品名称获取菜品相关信息--小程序传递参数：dishName:{}",map.get("name"));
        String name = map.get("name");
        int dishId = dishService.getDishByname(name).getId();
        // 布隆过滤器检查
        if (!bloomFilter.contains(String.valueOf(dishId))) {
            log.info("布隆过滤器拦截无效菜品ID查询: {}", dishId);
            return Result.success(null);
        }
        String dish = stringRedisTemplate.opsForValue().get(DISHID+dishId);
        if(dish != null && !dish.isEmpty()){
            Dish list = JSONUtil.toBean(dish, Dish.class);
            return Result.success(list);
        }
        Dish list = dishService.getDishById(dishId);
        log.info("redis存储商品分类信息");
        stringRedisTemplate.opsForValue().set(DISHID+dishId, JSONUtil.toJsonStr(list));
        return Result.success(list);
    }
}