package com.wxprogrem.service.impl;

import com.wxprogrem.mapper.SetMealDishMapper;
import com.wxprogrem.pojo.Dish;
import com.wxprogrem.pojo.SetMealDish;
import com.wxprogrem.service.SetMealDishService;
import com.wxprogrem.service.SetMealService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetMealDishImpl implements SetMealDishService {
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    public List<SetMealDish> getSetMealDishBySetMealId(int id) {
        return setMealDishMapper.getSetMealDishBySetMealId(id);
    }

    @Override
    public void delete(int setmealId) {
        setMealDishMapper.delete(setmealId);
    }

    @Override
    public void insert(int setmealId,List<SetMealDish> setMealDishList) {
        setMealDishMapper.insert(setmealId,setMealDishList);
    }

    @Override
    public List<SetMealDish> getBySetMealId(int setmealId) {
        return setMealDishMapper.getBySetMeaalId(setmealId);
    }
}
