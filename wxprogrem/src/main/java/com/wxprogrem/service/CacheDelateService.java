package com.wxprogrem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class CacheDelateService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Async("cacheDeleteExecutor")
    public void deleteCacheAsync(String... keys) {
        try {
            stringRedisTemplate.delete(Arrays.asList(keys));
            log.info("异步删除缓存成功，keys: {}", Arrays.toString(keys));
        } catch (Exception e) {
            log.error("异步删除缓存失败，keys: {}", Arrays.toString(keys), e);
            // 可记录失败日志，后续由定时任务补偿
        }
    }

}
