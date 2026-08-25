package com.wxprogrem.service.impl;

import com.wxprogrem.mapper.DishTypeMapper;
import com.wxprogrem.pojo.DishType;
import com.wxprogrem.service.DishTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishTypeServiceImpl implements DishTypeService {
    @Autowired
    private DishTypeMapper dishTypeMapper;
    @Override
    public List<DishType> getDishTypeList() {
        return dishTypeMapper.getDishTypeList();
    }

    @Override
    public void addDishType(DishType dishType) {
        dishTypeMapper.addDishType(dishType);
    }

    @Override
    public void updateDishType(DishType dishType) {
        dishTypeMapper.updateDishType(dishType);
    }

    @Override
    public void deleteDishType(Integer id) {
        dishTypeMapper.deleteDishType(id);
    }

    @Override
    public String getDishtype(int dishId) {
       return dishTypeMapper.getDishtype(dishId);
    }

    @Override
    public List<DishType> getDishtypeOnshow() {
        return dishTypeMapper.getDishtypeOnshow();
    }

    @Override
    public int getIdByType(String name) {
        return dishTypeMapper.getIdByType(name);
    }
}
