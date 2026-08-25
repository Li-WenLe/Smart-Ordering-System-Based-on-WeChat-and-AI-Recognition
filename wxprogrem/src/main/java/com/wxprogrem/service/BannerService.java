package com.wxprogrem.service;

import com.wxprogrem.pojo.Banner;
import com.wxprogrem.utils.Result;

import java.util.List;

public interface BannerService {

    //获取所有的轮播图
    List<Banner> getAllBanner();

    //添加轮播图
    void addBanner(Banner banner);

    //修改轮播图
    void updateBanner(Banner banner);

    //根据id删除轮播图
    void deleteById(int id);

    List<Banner> getOnshowBanner();

    Banner getBannerById(int id);
}
