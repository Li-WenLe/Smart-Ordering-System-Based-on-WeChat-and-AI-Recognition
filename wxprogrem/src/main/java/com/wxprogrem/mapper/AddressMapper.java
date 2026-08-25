package com.wxprogrem.mapper;

import com.wxprogrem.pojo.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressMapper {
    @Update("update address_book set is_default_address=1 where user_id=#{id}")
    void update(int id);

    @Insert("INSERT INTO address_book (user_id, phone, name, region, detail_address, is_default_address, tag) " +
            "VALUES (#{userId}, #{phone}, #{name}, #{region}, #{detailAddress}, #{isDefaultAddress}, #{tag})")
    void add(AddressBook addressBook);

    @Select("select *from address_book where user_id=#{id} and is_default_address=1")
    AddressBook getById(int id);

    @Select("select *from address_book where user_id=#{id}")
    List<AddressBook> getAll(int id);

    @Select("select *from address_book where id=#{id}")
    AddressBook getinfo(int id);

    @Update("update address_book set user_id=#{userId},phone=#{phone},name=#{name},region=#{region},detail_address=#{detailAddress},is_default_address=#{isDefaultAddress},tag=#{tag} where id=#{id}")
    void updateInfo(AddressBook addressBook);
}
