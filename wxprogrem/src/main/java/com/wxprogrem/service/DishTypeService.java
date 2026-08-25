package com.wxprogrem.service;

import com.wxprogrem.pojo.DishType;
import com.wxprogrem.utils.Result;

import java.util.List;

public interface DishTypeService {
    List<DishType> getDishTypeList();

    void addDishType(DishType dishType);

    void updateDishType(DishType dishType);

    void deleteDishType(Integer id);

    String getDishtype(int dishId);

    List<DishType> getDishtypeOnshow();

    int getIdByType(String name);
}
