package com.wxprogrem.controller.user;

import com.wxprogrem.pojo.Banner;
import com.wxprogrem.service.BannerService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Slf4j
@CrossOrigin
@RestController("userBannerController")
@RequestMapping("/user/banner")
@Tag(name="用户端轮播图",description = "用户端轮播图相关api")
public class BannerController {
    @Autowired
    private BannerService bannerService;
    @GetMapping
    @Operation(summary = "获取首页展示的轮播图信息",description = "获取首页展示的轮播图信息")
    public Result<List<Banner>> getOnshowBanner() {
       List<Banner>bannerList=bannerService.getOnshowBanner();
       return Result.success(bannerList);
    }

    @Operation(summary = "根据轮播图主键ID获取轮播图信息",description = "根据轮播图主键ID获取轮播图信息")
    @GetMapping("/getbyid")
    public Result<Banner> getBannerById(@RequestParam int id) {
        log.info("getBannerById id:{}",id);
        Banner banner=bannerService.getBannerById(id);
        return Result.success(banner);
    }
}
