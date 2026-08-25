package com.wxprogrem.service;

import com.wxprogrem.pojo.SetMealDish;

import java.util.List;

public interface SetMealDishService {
    List<SetMealDish> getSetMealDishBySetMealId(int id);

    void delete(int setmealId);

    void insert(int setmealId, List<SetMealDish> dishes);


    List<SetMealDish> getBySetMealId(int setmealId);
}
