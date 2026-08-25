package com.wxprogrem.service;

import com.wxprogrem.pojo.Voucher;

import java.util.List;

public interface VoucherService {
    List<Voucher> getAllVoucher();

    void updateVoucher(Voucher voucher);

    void addVoucher(Voucher voucher);

    void deleteVoucher(int id);

    Voucher getVoucherById(int voucherId);
}
