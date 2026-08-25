package com.wxprogrem.InventoryInitializer;

import com.wxprogrem.mapper.DishMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wxprogrem.constants.Constants.DISHINVENTORYID;

@Component
@Slf4j
public class InventoryInitializer {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 初始化redis缓存数据，首次使用打开，后续可以注释掉，否则每次启动会初始化redis商品的库存信息
     */

    @PostConstruct
    public void initInventory() {
        List<Integer> dishIds = dishMapper.getAllDishIds();
        if (dishIds.isEmpty()) {
            return;
        }
        Map<String, String> keyValueMap = new HashMap<>();
        for (Integer dishId : dishIds) {
            keyValueMap.put(DISHINVENTORYID + dishId, "100");
        }
        stringRedisTemplate.opsForValue().multiSet(keyValueMap);
        log.info("Initialized inventory for {} dishes to 100.", dishIds.size());
    }
}
