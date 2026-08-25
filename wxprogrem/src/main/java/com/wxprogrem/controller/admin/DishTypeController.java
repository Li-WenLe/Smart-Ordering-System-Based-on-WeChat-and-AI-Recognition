package com.wxprogrem.controller.admin;

import com.wxprogrem.pojo.DishType;
import com.wxprogrem.service.DishTypeService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController("/admin")
@RequestMapping("/dishtype")
@Tag(name="商家端菜品管理",description = "商家端菜品管理相关api")
public class DishTypeController {
    @Autowired
    private DishTypeService dishTypeService;

    @Operation(summary = "查询菜品分类的所有数据",description = "获取菜品分类的所有数据")
    @PostMapping                                               //获取dishType表种的所有数据
    public Result<List<DishType>>getDishType(){
        List<DishType> list = dishTypeService.getDishTypeList();
        return Result.success(list);
    }


    @Operation(summary = "添加菜品分类信息",description = "添加菜品分类")
    @PostMapping("/addtype")                                   //添加dishType,添加菜品种类
    public Result addDishType(@RequestBody DishType dishType){
        log.info("传递的参数：{}",dishType);
        dishTypeService.addDishType(dishType);
        return Result.success();
    }


    @Operation(summary = "修改菜品分类信息",description = "修改菜品分类")
    @PostMapping("/update")                                      //修改dishType表中的数据
    public Result updateDishType(@RequestBody DishType dishType){
        log.info("前端传递的参数：{}",dishType);
        dishTypeService.updateDishType(dishType);
        return Result.success();
    }


    @Operation(summary = "根据菜品分类ID删除菜品分类信息",description = "根据菜品分类ID删除菜品分类")    //根据id删除表中的数据
    @PostMapping("/delete")
    public Result deleteDishType(@RequestParam Integer id){
        log.info("前端传递的参数：{}",id);
        dishTypeService.deleteDishType(id);
        return Result.success();
    }
}
