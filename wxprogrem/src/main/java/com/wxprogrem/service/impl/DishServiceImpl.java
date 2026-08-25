package com.wxprogrem.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wxprogrem.mapper.DishMapper;
import com.wxprogrem.pojo.Dish;
import com.wxprogrem.pojo.PageBean;
import com.wxprogrem.service.DishService;
//import com.wxprogrem.utils.BloomFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    private List<Dish> getAllDishes() {

        return dishMapper.getAllDish();
    }
    @Override
    public PageBean<Dish> getAllDish(int pageSize, int pageNum) {
        PageBean<Dish> pageBean = new PageBean<>();              //构造PageBean
        PageHelper.startPage(pageNum, pageSize);                 //调用PageHelper
        List<Dish>dishList = dishMapper.getAllDish();            //获取存储所有菜品数据的列表
        Page<Dish>p=(Page<Dish>)dishList;                        //强制将列表转化为Page类型
        pageBean.setItems(p);
        pageBean.setTotal((int) p.getTotal());
       return pageBean;
    }


    @Override                                                      //根据菜品的分类Id和状态获取菜品
    public List<Dish> getDishByDishTypeIdAndStatus(int dishTypeId, int status) {
       return dishMapper.getDishByTypeAndStatus(dishTypeId,status);
    }

    @Override
    public void updateDish(Dish dish) {
        dish.setUpdateTime(LocalDateTime.now());                    //手动修改更新时间
        dishMapper.updateDish(dish);
    }

    @Override
    public void addDish(Dish dish) {
        dish.setUpdateTime(LocalDateTime.now());                    //手动设置修改时间
        dishMapper.addDish(dish);
    }

    @Override                                                       //通过分类id获取对应分类的所有菜品
    public List<Dish> getDishByTypeId(int dishTypeId) {
        return dishMapper.getDishByTypeId(dishTypeId);
    }

    @Override                                                       //通过菜品名称获取菜品
    public Dish getDishByName(String name) {

        return dishMapper.getDishByName(name);
    }

    @Override                                                       //通过id获取菜品
    public Dish getDishById(int id) {
        return dishMapper.getDishById(id);
    }

    @Override                                                       //获取所有处于启售状态的商品
    public List<Dish> getDishByIshow() {
        return dishMapper.getDishByIshow();
    }

    @Override                                                        //通过id删除菜品
    public void deleteDishById(int id) {
        dishMapper.deleteDishById(id);
    }

    @Override                                                        //TODO
    public int getDishTypeIdById(int id) {
        return dishMapper.getDishTypeIdById(id);
    }

    @Override                                                        //通过菜品名字获取菜品
    public Dish getDishByname(String name) {
        return dishMapper.getDishByname(name);
    }

    @Override
    public void dishInventoryDeduct(int id,int number) {
        dishMapper.dishInventoryDeduct(id,number);
    }
}
