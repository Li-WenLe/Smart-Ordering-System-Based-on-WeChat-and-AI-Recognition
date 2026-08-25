package com.wxprogrem.SpringTask;

import com.wxprogrem.mapper.DishMapper;
import com.wxprogrem.mapper.VoucherMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.wxprogrem.constants.Constants.*;

@Component
@Slf4j
public class SpringTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private DishMapper dishMapper;
    @Scheduled(cron = "0 0/20 * * * ?") // 每20分钟执行一次
    public void checkRedisVsDB() {
        log.info("定时任务开始执行******");
        //获取优惠券的所有id信息
        ArrayList<Integer> voucherIdList=voucherMapper.findAllVoucherId();
        for(int i=0;i<voucherIdList.size();i++){
            // 扫描 Redis 中剩余的库存数量，与数据库实际库存比对
            int voucherId=voucherIdList.get(i);
            int redisRemain= Integer.parseInt(stringRedisTemplate.opsForValue().get(STOCKSECKVOUCHERID+voucherId));
            int SqlRemain=voucherMapper.getRemainById(voucherId);
            // 如果发现差异，以数据库为准，强行修正 Redis 库存，并清理异常的购买记录
            if(redisRemain!=SqlRemain){
                //修复redi缓存中的优惠券库存信息
                stringRedisTemplate.opsForValue().set(STOCKSECKVOUCHERID+voucherId,String.valueOf(SqlRemain));
                log.info("数据库与缓存信息不一致，已自动修改");
            }
        }
        log.info("优惠券秒杀库存校验定时任务已完成");
        // 扫描 Redis 中剩余的库存数量，与数据库实际库存比对
        //获取所有有效dishID
        List<Integer>dishIdList=dishMapper.getAllDishIds();
        for(int i=0;i<dishIdList.size();i++){
            int sqlRemainInventory=dishMapper.getInventoryByDishId(dishIdList.get(i));
            int redisRemainInventory= Integer.parseInt(stringRedisTemplate.opsForValue().get(DISHINVENTORYID+dishIdList.get(i)));
            if(sqlRemainInventory!=redisRemainInventory){
                //修改redis缓存，以数据库为准
                stringRedisTemplate.opsForValue().set(DISHINVENTORYID+dishIdList.get(i),String.valueOf(sqlRemainInventory));
            }
        }
        log.info("菜品库存校验定时任务已完成");
    }


}
