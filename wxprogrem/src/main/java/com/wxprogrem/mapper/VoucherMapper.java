package com.wxprogrem.mapper;

import com.wxprogrem.pojo.Voucher;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface VoucherMapper {
    @Select("select *from voucher")
    List<Voucher> getAllVoucher();

    @Update("Update voucher set title=#{title},ruler=#{ruler},used_time=#{usedTime},cover=#{cover},number=#{number},remain=#{remain} where id=#{id}")
    void updateVoucher(Voucher voucher);

    @Insert("insert into voucher (title,ruler,used_time,cover,number,remain) values (#{title},#{ruler},#{usedTime},#{cover},#{number},#{remain})")
    void addVoucher(Voucher voucher);

    @Delete("delete from voucher where id=#{id} ")
    void deleteVoucher(int id);

    @Update("update voucher set remain=#{remain}-1 where id=#{id}")
    void updateVoucherRemain(int remain);

    @Select("select *from voucher where id=#{voucherId}")
    Voucher getVoucherById(@Param("voucherId") int voucherId);

    @Update("update voucher set remain=remain-1 where id=#{voucherId} and remain>0")
    int decreaseVoucher(@Param("voucherId") Integer voucherId);

    @Select("select id from voucher")
    ArrayList<Integer> findAllVoucherId();

    @Select("select remain from  voucher where id=#{voucehrId}")
    Integer getRemainById(int voucherId);
}
