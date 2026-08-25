-- KEYS[1] : 库存Key (stock:voucher:1001)
-- KEYS[2] : 用户领取记录Key (bought:voucher:1001)
-- ARGV[1] : 用户ID
-- ARGV[2] : 限购数量（这里写死为1，但预留参数）

-- 1. 幂等性校验：检查用户是否已经抢过
local isMember = redis.call('sismember', KEYS[2], ARGV[1])
if isMember == 1 then
    return -1  -- 用户重复领取，直接拦截
end

-- 2. 原子性扣减库存（先减后判断，避免并发问题）
local remain = redis.call('decr', KEYS[1])

-- 3. 判断是否超卖（如果扣减后小于0，说明库存不足）
if remain < 0 then
    -- 扣多了，立刻回滚加回去
    redis.call('incr', KEYS[1])
    return -2  -- 库存不足
end

-- 4. 扣减成功，记录用户ID到Set中
redis.call('sadd', KEYS[2], ARGV[1])

-- 5. 返回剩余库存（大于等于0）
return remain