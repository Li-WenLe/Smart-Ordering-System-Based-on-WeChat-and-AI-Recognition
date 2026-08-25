package com.wxprogrem.mapper;

import com.wxprogrem.pojo.SetMeal;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetMealMapper {
    @Select("select*from setmeal")
    List<SetMeal> getAllSetMeal();

    @Insert("insert into setmeal(name,price,status,description,cover) values (#{name},#{price},#{status},#{description},#{cover})")
    void addSetMeal(SetMeal setMeal);

    @Update("update setmeal set name=#{name},status=#{status},cover=#{cover},price=#{price},description=#{description} where id=#{id}")
    void updateSetMeal(SetMeal setMeal);

    @Select("select *from setmeal where id=#{id}")
    SetMeal getSetMealById(int id);

    @Select("select *from setmeal where name=#{name}")
    SetMeal getSetMealByName(String name);

    @Delete("delete from setmeal where id=#{setmealId}")
    void delete(int setmealId);
}
