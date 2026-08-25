package com.wxprogrem.mapper;

import com.wxprogrem.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    @Select("select *from user where username=#{username}and password=#{password}")
    User selectByUserNameAndPassword(String username, String password);

    @Insert("insert into user (username,password,name,phone,photo,register_time) values(#{username},#{password},#{name},#{phone},#{photo},#{registerTime})")
    void add(User newUser);

    @Select("select *from user where username=#{username}")
    User getUserByUsername(String username);


    List<Map<String, Object>> countDailyRegistrations(LocalDate startDate, LocalDate endDate);
}
