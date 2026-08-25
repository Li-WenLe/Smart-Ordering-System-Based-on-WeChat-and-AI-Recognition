package com.wxprogrem.mapper;

import com.wxprogrem.pojo.SickVoucher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SickVoucherMapper {
    @Select("select *from sick_voucher where user_id=#{userId} and voucher_id=#{voucherId}")
    SickVoucher getBooleanBuy(int userId, int voucherId);

    @Insert("insert into sick_voucher(user_id,voucher_id) values (#{userId},#{voucherId})")
    void insert(int userId, int voucherId);
}
