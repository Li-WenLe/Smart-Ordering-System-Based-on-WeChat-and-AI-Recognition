package com.wxprogrem.mapper;

import com.wxprogrem.pojo.Dish;
import com.wxprogrem.pojo.SetMealDish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    @Select("select*from setmeal_dish where setmeal_id=#{id}")
    List<SetMealDish> getSetMealDishBySetMealId(int id);

    @Update("update setmeal_dish set name=")
    void update(SetMealDish setMealDish);

    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void delete(int setmealId);


    void insert(int setmealId, List<SetMealDish> setMealDishList);

    @Select("select *from setmeal_dish where name=#{name}")
    SetMealDish getSetMealDishByName(String name);

    @Select("select *from setmeal_dish where setmeal_id=#{setmealId}")
   List<SetMealDish> getBySetMeaalId(int setmealId);
}
