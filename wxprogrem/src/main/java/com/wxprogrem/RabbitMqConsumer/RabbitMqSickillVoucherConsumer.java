package com.wxprogrem.RabbitMqConsumer;

import com.rabbitmq.client.Channel;
import com.wxprogrem.config.RabbitMqConfiguration;
import com.wxprogrem.config.RedissonConfig;
import com.wxprogrem.dto.VoucherDeductDTO;
import com.wxprogrem.mapper.VoucherMapper;
import com.wxprogrem.mapper.VoucherUserMapper;
import com.wxprogrem.pojo.VoucherUser;
import com.wxprogrem.service.SickillVoucherBoughtService;
import com.wxprogrem.service.SickillVoucherService;
import io.swagger.v3.oas.annotations.headers.Header;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.wxprogrem.constants.Constants.*;

@Component
@Slf4j
public class RabbitMqSickillVoucherConsumer {
    @Autowired
    private VoucherUserMapper voucherUserMapper;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SickillVoucherBoughtService sickillVoucherBoughtServiceService;
    /**
     * 写入优惠券秒杀结果到数据库
     *
     */
    @RabbitListener(queues = RabbitMqConfiguration.SICKILLVOUCHER_QUEUE)
    public void RabbitMqSickillVoucherConsumer(VoucherDeductDTO dto) {
        log.info("优惠券秒杀消费者已经收到生产者消息***********");
        Integer voucherId = dto.getVoucherId();
        Integer userId = dto.getUserId();
        //幂等校验，检查是否已经入库
        VoucherUser voucherUser=voucherUserMapper.SelectVoucher(userId,voucherId);
        if(voucherUser!=null){
            return;
        }
        //加分布式锁
        RLock lock = redissonClient.getLock( BOUGHTSECKILLLOCK+voucherId);
        try {
            boolean isLocked= lock.tryLock(5,10, TimeUnit.SECONDS);
            if(!isLocked) {//扣减优惠券,用户加券
                log.error("获取分布式锁失败，voucherId: {}", voucherId);
                throw new RuntimeException("系统繁忙，请稍后重试");
            }
            sickillVoucherBoughtServiceService.sickillVoucherBought(voucherId,userId);
            log.info("消息队列入库成功**************&**");

        } catch (InterruptedException e) {
            log.error("处理秒杀失败: {}", e.getMessage());
            // 只有 DB 库存不足报错时，才需要回滚 Redis
            if (e.getMessage() != null && e.getMessage().contains("DB库存不足")) {
                rollbackRedisStock(voucherId, userId);
            }
            // 如果是其他异常（如 SQL 异常），事务已自动回滚DB，Redis无需操作
            throw new RuntimeException(e); // 让 RabbitMQ 触发重试或进入死信队列


        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void rollbackRedisStock(Integer voucherId, Integer userId) {
        // 回滚库存 +1
        stringRedisTemplate.opsForValue().increment(STOCKSECKVOUCHERID+ voucherId);
        // 移除用户领取记录
        stringRedisTemplate.opsForSet().remove(BOUGHTSECKILLVOUCHER + voucherId, userId.toString());
        log.warn("Redis库存已回滚，优惠券idvoucherId: {}, 用户iduserId: {}", voucherId, userId);
    }


}
