package com.wxprogrem.mapper;

import com.wxprogrem.pojo.VoucherUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VoucherUserMapper {
    @Select("select *from voucher_user where user_id=#{userId}")
    List<VoucherUser> getAllByUserId(int userId);

    @Update("update voucher_user set acount=acount-1 where user_id=#{userId} and voucher_id=#{voucherId}")
    void Usedvoucher(int userId, int voucherId);


    @Insert("insert into voucher_user(user_id,voucher_id,acount) values (#{userId},#{voucherId},#{acount})")
    void insert(@Param("userId") int userId, @Param("voucherId") int voucehrId, @Param("acount") int acount);

    @Insert("insert into voucher_user(user_id,voucher_id,acount) values (#{userId},#{voucherId},#{acount})")
    void addVoucher(int userId, int voucherId,int acount);

    @Delete("delete from voucher_user where user_id=#{userId} and voucher_id=#{voucherId}")
    void deleteVoucher(int userId, int voucherId);

    @Select("select acount from voucher_user where user_id=#{userId} and voucher_id=#{voucherId}")
    Integer getAcount(int userId, int voucherId);
    @Select("SELECT * FROM voucher_user where user_id=#{userId} and voucher_id=#{voucherId}")
    VoucherUser SelectVoucher(int userId,int voucherId);

    @Update("update voucher_user set acount=acount+1 where voucher_id=#{voucherId} and user_id=#{userId}")
    void increaseVoucherCount(@Param("userId") int userId,@Param("voucherId") int voucherId);
}
