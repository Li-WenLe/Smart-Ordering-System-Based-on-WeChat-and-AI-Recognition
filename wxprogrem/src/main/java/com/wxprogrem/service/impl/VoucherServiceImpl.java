package com.wxprogrem.service.impl;

import com.wxprogrem.mapper.VoucherMapper;
import com.wxprogrem.pojo.Voucher;
import com.wxprogrem.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherMapper voucherMapper;
    @Override
    public List<Voucher> getAllVoucher() {
        return voucherMapper.getAllVoucher();
    }

    @Override
    public void updateVoucher(Voucher voucher) {
        voucherMapper.updateVoucher(voucher);
    }

    @Override
    public void addVoucher(Voucher voucher) {
        voucherMapper.addVoucher(voucher);
    }

    @Override
    public void deleteVoucher(int id) {
        voucherMapper.deleteVoucher(id);
    }

    @Override
    public Voucher getVoucherById(int voucherId) {
        return voucherMapper.getVoucherById(voucherId);
    }
}
