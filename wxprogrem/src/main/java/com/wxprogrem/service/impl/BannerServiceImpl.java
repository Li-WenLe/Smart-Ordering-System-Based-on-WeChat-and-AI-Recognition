package com.wxprogrem.service.impl;

import com.wxprogrem.mapper.BannerMapper;
import com.wxprogrem.pojo.Banner;
import com.wxprogrem.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerMapper bannerMapper;
    @Override
    public List<Banner> getAllBanner() {
        return bannerMapper.getAllBanner();
    }

    @Override
    public void addBanner(Banner banner) {
        bannerMapper.addBanner(banner);
    }

    @Override
    public void updateBanner(Banner banner) {
        bannerMapper.updateBanner(banner);
    }

    @Override
    public void deleteById(int id) {
        bannerMapper.deleteById(id);
    }

    @Override
    public List<Banner> getOnshowBanner() {
       return bannerMapper.getOnshowBanner();
    }

    @Override
    public Banner getBannerById(int id) {
        return bannerMapper.getBannerById(id);
    }
}
