package com.wxprogrem.controller.admin;

import cn.hutool.json.JSONUtil;
import com.wxprogrem.config.RabbitMqConfiguration;
import com.wxprogrem.dto.VoucherDeductDTO;
import com.wxprogrem.pojo.Dish;
import com.wxprogrem.pojo.PageBean;
import com.wxprogrem.service.DishService;
import com.wxprogrem.service.DishTypeService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.TypeReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wxprogrem.config.RabbitMqConfiguration.*;
import static com.wxprogrem.constants.Constants.*;

@Slf4j
@CrossOrigin
@RestController("admin")
@RequestMapping("/dish")
@Tag(name="商家管理端菜品管理",description = "商家管理端菜品管理相关api")
public class DishController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DishService dishService;
    @Autowired
    private DishTypeService dishTypeService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "菜品分页查询",description = "菜品分页查询")
    @GetMapping                                                       //菜品分页查询
    public Result<PageBean<Dish>> getAllDish(@RequestParam int pageSize, @RequestParam int pageNum) {
        log.info("分页查询--前端传递参数:pageSize:{},pageNum:{}", pageSize, pageNum);
        PageBean<Dish>dishList=dishService.getAllDish(pageSize,pageNum);
        return Result.success(dishList);
    }


    @Operation(summary = "获取某一分类下所有处于启售？停售状态下的商品",description = "获取某一分类下所有处于启售？停售状态下的商品")
    @GetMapping("/getdish")                                            //获取某一分类下所有处于启售？停售状态下的商品
    public Result<List<Dish>> getDishByDishTypeIdAndStatus(@RequestParam int dishTypeId,@RequestParam int status) {
        log.info("获取某一分类下所有处于启售？停售状态下的商品--前端传递的参数：dishTypeId:{},status:{}",dishTypeId,status);
        //查询redis中有无相关缓存
        String redisStoreListJson=stringRedisTemplate.opsForValue().get(DISHTYPEIDWITHSTATUS+dishTypeId+"_"+status);
        //如果存在，直接返回
        if(redisStoreListJson!=null&&!redisStoreListJson.isEmpty()){
            List<Dish>List= JSONUtil.toList(redisStoreListJson,Dish.class);
            return Result.success(List);
        }
        //SQL查找
        List<Dish> list=dishService.getDishByDishTypeIdAndStatus(dishTypeId,status);
        //存入缓存
        stringRedisTemplate.opsForValue().set(DISHTYPEIDWITHSTATUS+dishTypeId+"_"+status,JSONUtil.toJsonStr(list));
        return Result.success(list);
    }


    @Operation(summary = "修改菜品信息",description = "修改菜品信息")
    @PostMapping("/update")                                             //修改菜品信息
    public Result updateDish(@RequestBody Dish dish) {
        log.info("修改菜品信息--前端传递的参数：{}",dish);
        //先删后改，维护缓存和数据库的统一性
        int dishId=dish.getId();
        //删除菜品缓存信息
        stringRedisTemplate.delete(DISHID+dishId);
        //删除菜品对应的分类的缓存信息
        int dishTypeId=dish.getDishTypeId();
        stringRedisTemplate.delete(DISHTYPEID+dishTypeId);
        //修改菜品的数据库信息
        dishService.updateDish(dish);
        //发送延时消息到延时事务队列，实现第二次删除
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,DISHID+dishId);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,DISHTYPEID+dishTypeId);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,DISHID+dishId);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,DISHTYPEID+dishTypeId);
        return Result.success();
    }



    @Operation(summary = "根据菜品id获取菜品的分类",description = "根据菜品id获取菜品的分类")
    @GetMapping("/getdishtype")                                         //根据菜品id获取菜品的分类
    public Result getDishtype(@RequestParam int dishId) {
        return Result.success(dishTypeService.getDishtype(dishId));
    }


    @Operation(summary = "添加菜品信息",description = "添加菜品信息")
    @PutMapping
    public Result addDish(@RequestBody Dish dish) {                        //添加菜品信息
        log.info("添加菜品--前端传递的参数:Dish{}", dish);
        //TODO删除首页onshow的redis缓存
        stringRedisTemplate.delete(DISHONSHOW);
        //删除该商品分类对应的缓存
        int dishTypeId=dish.getDishTypeId();
        stringRedisTemplate.delete(DISHTYPEID+dishTypeId);
        dishService.addDish(dish);
        //发送延时消息到延时事务队列，实现延时双删
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,DISHONSHOW);
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,DISHTYPEID+dishTypeId);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,DISHONSHOW);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,DISHTYPEID+dishTypeId);
        return Result.success();
    }


    @Operation(summary = "根据菜品分类ID获取对应分类下的所有菜品",description = "根据菜品分类ID获取对应分类下的所有菜品")
    @PostMapping("/getdishbytypeid")                                        //获取某分类下的所有菜品
    public Result<List<Dish>> getDishByTypeId(@RequestParam int dishTypeId) {
        log.info("获取某分类下的所有菜品--分类id:dishTypeId:{}",dishTypeId);
        //查询redis中有无相关缓存
        String redisStoreListJson=stringRedisTemplate.opsForValue().get(DISHTYPEID+dishTypeId);
        if(redisStoreListJson!=null&&!redisStoreListJson.isEmpty()){
            List<Dish>List=JSONUtil.toList(redisStoreListJson,Dish.class);
            return Result.success(List);
        }
        //SQL查找
        List<Dish>list= dishService.getDishByTypeId(dishTypeId);
        //存入缓存
        stringRedisTemplate.opsForValue().set(DISHTYPEID+dishTypeId,JSONUtil.toJsonStr(list));
        return Result.success(list);
    }


    @Operation(summary = "删除菜品",description = "删除菜品")
    @PostMapping("/delete")
    public Result deleteDish(@RequestParam int id) {
        //TODO 先删除分类信息的缓存，在删除数据库的商品和redis缓存
        log.info("删除的商品id:{}",id);
        //删除首页onshow的redis缓存
        stringRedisTemplate.delete(DISHONSHOW);
        //删除对应的商品的redis缓存
        log.info("删除的redis key值：{}",DISHID+id);
        stringRedisTemplate.delete(DISHID+id);
        //删除对应分类的redis缓存
        int dishTypeId=dishService.getDishTypeIdById(id);
        log.info("该商品对应的分类id:{}",dishTypeId);
        stringRedisTemplate.delete(DISHTYPEID+dishTypeId);
        dishService.deleteDishById(id);
        //延时双删
        rabbitTemplate.convertAndSend(DELAYED_BUSINESS_EXCHANGE,DELAYED_BUSINESS_QUEUE,DISHID+id);
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                DELAYED_BUSINESS_EXCHANGE, DELAYED_BUSINESS_QUEUE,DISHID+id);
        return Result.success();
    }
}
