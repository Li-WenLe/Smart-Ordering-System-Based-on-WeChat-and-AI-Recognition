package com.wxprogrem.BloomFilter;


import com.wxprogrem.config.RedissonConfig;
import com.wxprogrem.mapper.DishMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BloomFilterService {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private DishMapper dishMapper;

    @Bean
    @ConditionalOnMissingBean
    public RBloomFilter<String> bloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("dishIdBloomFilter");
        // 初始化：预期插入数量、误判率
        bloomFilter.tryInit(10000, 0.01);
        //获取所有菜品的有效ID
        List<Integer> dishIdList=dishMapper.getAllDishIds();
        for (Integer dishId : dishIdList) {
            bloomFilter.add(String.valueOf(dishId));
        }
        log.info("布隆过滤器创建成功：dishIdBloomFilter:{}",bloomFilter);
        return bloomFilter;
    }
}
