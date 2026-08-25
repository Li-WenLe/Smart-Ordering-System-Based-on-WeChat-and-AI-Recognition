package com.wxprogrem.service;

import com.wxprogrem.pojo.Dish;
import com.wxprogrem.pojo.PageBean;
import com.wxprogrem.utils.Result;

import java.util.List;

public interface DishService {
    PageBean<Dish> getAllDish(int pageSize, int pageNum);

    List<Dish> getDishByDishTypeIdAndStatus(int dishTypeId, int status);

    void updateDish(Dish dish);

    void addDish(Dish dish);

    List<Dish> getDishByTypeId(int dishTypeId);

    Dish getDishByName(String name);

    Dish getDishById(int id);

    List<Dish> getDishByIshow();

    void deleteDishById(int id);

    int getDishTypeIdById(int id);

    Dish getDishByname(String name);

    void dishInventoryDeduct(int id,int number);
}
