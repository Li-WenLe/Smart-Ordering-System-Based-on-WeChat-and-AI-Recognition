package com.wxprogrem.mapper;

import com.wxprogrem.pojo.Banner;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BannerMapper {
    @Select("select * from banner")
    List<Banner> getAllBanner();

    @Insert("insert into banner(content,cover,ishow,title,image) values (#{content},#{cover},#{ishow},#{title},#{image})")
    void addBanner(Banner banner);

    @Update("update banner set cover=#{cover},content=#{content},ishow=#{ishow},image=#{image},title=#{title} where id=#{id}")
    void updateBanner(Banner banner);

    @Delete("delete from banner where id=#{id}")
    void deleteById(int id);

    @Select("select *from banner where ishow=0")
    List<Banner> getOnshowBanner();

    @Select("select *from banner where id=#{id}")
    Banner getBannerById(int id);
}
