package com.wxprogrem.mapper;

import com.wxprogrem.pojo.DishType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishTypeMapper {
    @Select("select *from dish_type")
    List<DishType> getDishTypeList();

    @Insert("insert into dish_type (type,ishow,cover) values (#{type},#{ishow},#{cover})")
    void addDishType(DishType dishType);

    @Update("update dish_type set type=#{type},ishow=#{ishow},cover=#{cover} where id=#{id}")
    void updateDishType(DishType dishType);

    @Delete("delete from dish_type where id=#{id}")
    void deleteDishType(Integer id);

    @Select("select type from dish_type where dish_id=#{dishId}")
    String getDishtype(int dishId);

    @Select("select *from dish_type where ishow=0")
    List<DishType> getDishtypeOnshow();

    @Select("select id from dish_type where type=#{name}")
    int getIdByType(String name);
}
