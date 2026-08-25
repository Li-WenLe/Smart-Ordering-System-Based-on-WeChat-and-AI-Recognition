package com.wxprogrem.service;

import com.wxprogrem.pojo.VoucherUser;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface VoucherUserService {
    List<VoucherUser> getAllByUserId(int userId);

    void Usedvoucher(int userId, int voucherId);

    void addVoucher(int userId, int voucherId,int acount);

    void deleteVoucher(int userId, int voucherId);

    Integer getAcount(int userId, int voucherId);

    void increaseVoucherCount(int userId, int voucherId);

}
