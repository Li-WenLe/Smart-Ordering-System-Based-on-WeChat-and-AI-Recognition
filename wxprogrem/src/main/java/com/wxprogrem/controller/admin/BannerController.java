package com.wxprogrem.controller.admin;

import com.wxprogrem.pojo.Banner;
import com.wxprogrem.service.BannerService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/banner")
@Tag(name="轮播图管理",description = "轮播图管理相关api")
public class BannerController {

    @Autowired
    private BannerService bannerService;
    @Operation(summary = "查询所有轮播图信息",description = "获取所有轮播图信息")
    @GetMapping                                                  //获取所有轮播图信息
    public Result<List<Banner>> getAllBanner(){
        log.info("获取所有轮播图信息************");
        List<Banner>list=bannerService.getAllBanner();
        return Result.success(list);
    }


    @Operation(summary="添加轮播图信息",description = "添加轮播图")
    @PostMapping("/add")                                         //添加轮播图
    public Result addBanner(@RequestBody Banner banner){
        log.info("添加轮播图--前端传递的参数：banner:{}",banner);
        bannerService.addBanner(banner);
        return Result.success();
    }


    @Operation(summary="修改轮播图信息",description = "修改轮播图")
    @PostMapping("/update")                                      //修改轮播图
    public Result updateBanner(@RequestBody(required = true) Banner banner){
        log.info("修改轮播图--前端传递@Parameter(name = \"id\", description = \"编号\", required = true, example = \"1024\")的参数：banner:{}",banner);
        bannerService.updateBanner(banner);
        return Result.success();
    }


    @Operation(summary="根据轮播图id删除轮播图信息",description = "删除轮播图")
    @DeleteMapping("/delete")                                    //删除轮播图
    public Result deleteBanner(@RequestParam int id){
        log.info("删除轮播图--前端传递的参数:轮播图id:{}",id);
        bannerService.deleteById(id);
        return Result.success();
    }
}
