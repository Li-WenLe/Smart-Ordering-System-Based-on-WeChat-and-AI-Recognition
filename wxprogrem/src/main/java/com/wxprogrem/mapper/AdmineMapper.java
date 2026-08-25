package com.wxprogrem.mapper;

import com.wxprogrem.pojo.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdmineMapper {
    @Select("select *from employee where username=#{username},password=#{password}")
    Employee login(String username, String password);

    @Select("select *from employee where username=#{username}")
    Employee getEmployeeByUsername(String username);

    @Insert("insert into employee (username,password) values (#{username},#{password})")
    void regist(String username, String password);
}
