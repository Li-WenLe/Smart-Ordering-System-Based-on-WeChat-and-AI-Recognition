package com.wxprogrem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.wxprogrem.constants.Constants.BOUGHTSECKILLVOUCHER;
import static com.wxprogrem.constants.Constants.STOCKSECKVOUCHERID;

@Component
public class SickillVoucherService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static DefaultRedisScript<Long> SCRIPT = null;
    static {
        SCRIPT=new DefaultRedisScript<>();
        String scriptText =
                "local isMember = redis.call('sismember', KEYS[2], ARGV[1]) " +
                        "if isMember == 1 then return -1 end " +
                        "local remain = redis.call('decr', KEYS[1]) " +
                        "if remain < 0 then " +
                        "   redis.call('incr', KEYS[1]) " +
                        "   return -2 " +
                        "end " +
                        "redis.call('sadd', KEYS[2], ARGV[1]) " +
                        "return remain";
        SCRIPT.setScriptText(scriptText);
        SCRIPT.setResultType(Long.class); // 返回类型必须是Long

    }
    /**
     * 执行秒杀预扣减
     * @return -1:重复领取, -2:库存不足, >=0:扣减成功(返回剩余库存)
     */
    public long tryAcquireVoucher(Long voucherId, Integer userId) {
        List<String> keys = Arrays.asList(
                STOCKSECKVOUCHERID + voucherId,
                BOUGHTSECKILLVOUCHER+ voucherId
        );
        // ARGV传参
        List<String> args = Arrays.asList(
                String.valueOf(userId),
                "1" // 限购数量（预留）
        );

        // 执行脚本
        Long result = stringRedisTemplate.execute(SCRIPT, keys, args.toArray());
        return result != null ? result : -999;
    }
}
