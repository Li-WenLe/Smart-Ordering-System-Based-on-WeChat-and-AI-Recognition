package com.wxprogrem.service;

import com.wxprogrem.pojo.SetMeal;

import java.util.List;

public interface SetMealService {
    List<SetMeal> getAllSetMeal();

    void addSetMeal(SetMeal setMeal);

    void updateSetMeal(SetMeal setMeal);

    SetMeal getSetMealById(int id);

    SetMeal getSetMealByName(String name);

    void delete(int setmealId);
}
