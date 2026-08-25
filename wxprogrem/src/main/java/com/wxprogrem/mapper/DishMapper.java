package com.wxprogrem.mapper;

import com.wxprogrem.pojo.Dish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {
    @Select("select *from dish")
    List<Dish> getAllDish();

    @Select("select id from dish")
    List<Integer>getAllDishIds();

    @Select("select * from dish where dish_type_id=#{type} and status=#{status}")
    List<Dish> getDishByTypeAndStatus(int type, int status);

    @Update("update dish set name=#{name},description=#{description},price=#{price},image=#{image},status=#{status},dish_type_id=#{dishTypeId},update_time=#{updateTime}, recommend=#{recommend} where id=#{id}")
    void updateDish(Dish dish);

    @Insert("INSERT INTO dish (name, price, image, description, status, dish_type_id,  update_time, recommend) " +
            "VALUES (#{name}, #{price}, #{image}, #{description}, #{status}, #{dishTypeId}, now(),#{recommend})")
    void addDish(Dish dish);

    @Select("select *from dish where dish_type_id=#{dishTypeId}")
    List<Dish> getDishByTypeId(int dishTypeId);

    @Select(("select *from dish where name=#{name}"))
    Dish getDishByName(String name);

    @Select("select *from dish where id=#{id}")
    Dish getDishById(int id);

    @Select("select *from dish where status=0")
    List<Dish> getDishByIshow();

    @Delete("delete from dish where id=#{id}")
    void deleteDishById(int id);

    @Select("select dish_type_id from dish where id=#{id}")
    int getDishTypeIdById(int id);

    @Select("select *from dish where name=#{name}")
    Dish getDishByname(String name);
    @Update("update dish set inventory=inventory-#{number} where id=#{id}")
    void dishInventoryDeduct(int id,int number);

    @Select("select inventory from dish where id=#{id}")
    int getInventoryByDishId(int id);

}
