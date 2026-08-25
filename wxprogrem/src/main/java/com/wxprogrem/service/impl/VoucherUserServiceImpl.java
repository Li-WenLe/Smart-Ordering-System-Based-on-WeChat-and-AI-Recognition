package com.wxprogrem.service.impl;

import com.wxprogrem.mapper.VoucherUserMapper;
import com.wxprogrem.pojo.VoucherUser;
import com.wxprogrem.service.VoucherUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherUserServiceImpl implements VoucherUserService {
    @Autowired
    private VoucherUserMapper voucherUserMapper;
    @Override
    public List<VoucherUser> getAllByUserId(int userId) {
        return voucherUserMapper.getAllByUserId(userId);
    }

    @Override
    public void Usedvoucher(int userId, int voucherId) {
        voucherUserMapper.Usedvoucher(userId,voucherId);
    }

    @Override
    public void addVoucher(int userId, int voucherId,int acount) {
        voucherUserMapper.addVoucher(userId,voucherId,acount);
    }

    @Override
    public void deleteVoucher(int userId, int voucherId) {
        voucherUserMapper.deleteVoucher(userId,voucherId);
    }

    @Override
    public Integer getAcount(int userId, int voucherId) {
        return voucherUserMapper.getAcount(userId,voucherId);
    }

    @Override
    public void increaseVoucherCount(int userId, int voucherId) {
        voucherUserMapper.increaseVoucherCount(userId,voucherId);
    }
}
