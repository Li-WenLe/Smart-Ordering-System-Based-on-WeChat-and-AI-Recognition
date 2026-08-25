package com.wxprogrem.service.impl;

import com.wxprogrem.mapper.SetMealMapper;
import com.wxprogrem.pojo.SetMeal;
import com.wxprogrem.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    @Override
    public List<SetMeal> getAllSetMeal() {
        return setMealMapper.getAllSetMeal();
    }

    @Override
    public void addSetMeal(SetMeal setMeal) {
        setMealMapper.addSetMeal(setMeal);
    }

    @Override
    public void updateSetMeal(SetMeal setMeal) {
        setMealMapper.updateSetMeal(setMeal);
    }

    @Override
    public SetMeal getSetMealById(int id) {
        return setMealMapper.getSetMealById(id);
    }

    @Override
    public SetMeal getSetMealByName(String name) {
        return setMealMapper.getSetMealByName(name);
    }

    @Override
    public void delete(int setmealId) {
        setMealMapper.delete(setmealId);
    }
}
