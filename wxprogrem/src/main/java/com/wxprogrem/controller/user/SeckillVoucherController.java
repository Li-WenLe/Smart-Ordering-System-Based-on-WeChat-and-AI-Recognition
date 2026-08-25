package com.wxprogrem.controller.user;

import com.wxprogrem.dto.VoucherDeductDTO;
import com.wxprogrem.mapper.SickVoucherMapper;
import com.wxprogrem.service.SickillVoucherService;
import com.wxprogrem.service.VoucherService;
import com.wxprogrem.service.VoucherUserService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.wxprogrem.config.RabbitMqConfiguration.SICKILLVOUCHER_QUEUE;
import static com.wxprogrem.config.RabbitMqConfiguration.SICKILLVOUCH_EXCHANGE;

@CrossOrigin
@RestController("/userSeckillVoucherController")
@RequestMapping("/user/seckill")
@Transactional
@Slf4j
@Tag(name = "用户端优惠券秒杀相关接口",description = "用户端优惠券秒杀相关api")
public class SeckillVoucherController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private VoucherUserService voucherUserService;
    @Autowired
    private SickVoucherMapper sickVoucherMapper;
    @Autowired
    private SickillVoucherService sickillVoucherService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Operation(summary = "用户端优惠券秒杀",description = "用户端优惠券秒杀")
    @PostMapping("/seckill")
    public Result sickVoucher(@RequestBody Map<String, Integer> map) {
        int userId = map.get("userId");
        int voucherId = map.get("voucherId");
        log.info("userId:{}", userId);
        log.info("voucherId:{}", voucherId);
        long result = sickillVoucherService.tryAcquireVoucher((long) voucherId, userId);
        if (result == -1) {
            return Result.error("您已领取过该优惠券");
        }
        if (result == -2) {
            return Result.error("优惠券已售罄");
        }
        rabbitTemplate.convertAndSend(
                SICKILLVOUCH_EXCHANGE,
                SICKILLVOUCHER_QUEUE,
                new VoucherDeductDTO(voucherId, userId)
        );
        log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                SICKILLVOUCH_EXCHANGE, SICKILLVOUCHER_QUEUE, new VoucherDeductDTO(voucherId, userId));
        // 4. 秒杀成功，立即返回（响应时间 < 50ms）
        return Result.success("抢购成功，正在处理中");
    }
}

