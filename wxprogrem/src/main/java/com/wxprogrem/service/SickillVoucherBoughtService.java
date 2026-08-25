package com.wxprogrem.service;

import com.wxprogrem.controller.user.DishController;
import com.wxprogrem.mapper.VoucherMapper;
import com.wxprogrem.mapper.VoucherUserMapper;
import com.wxprogrem.pojo.VoucherUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SickillVoucherBoughtService {
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private VoucherUserService voucherUserService;
    @Autowired
    private DishController user;

    // rollbackFor = Exception.class 保证任何异常都回滚（尤其是 checked 异常）
    @Transactional(rollbackFor = Exception.class)
    public void sickillVoucherBought(Integer voucherId, Integer userId) {
        // 1. 扣减库存
        int affectedRows = voucherMapper.decreaseVoucher(voucherId);

        // 2. 如果库存不足，抛出运行时异常，触发事务回滚
        if (affectedRows == 0) {
            throw new RuntimeException("DB库存不足");
        }

        // 3. 增加用户券数量（如果这里抛出异常，第1步的扣减也会自动回滚）
        //判断表中是否存在数据
        Integer acount = voucherUserService.getAcount(userId, voucherId);
        if (acount ==null) {
            voucherUserService.addVoucher(userId,voucherId,1);
        }else{
            voucherUserService.increaseVoucherCount(userId, voucherId);
        }
        log.info("DB事务提交成功，voucherId: {}, userId: {}", voucherId, userId);
    }
}
