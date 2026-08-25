package com.wxprogrem.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    @Schema(name="菜品ID",description = "菜品ID")
    private int id;
    @Schema(name="菜品名称",description = "菜品名称")
    private String name;
    @Schema(name="菜品价格",description = "菜品价格")
    private Double price;
    @Schema(name="菜品封面图",description = "菜品封面图")
    private String image;
    @Schema(name="菜品简介",description = "菜品简介")
    private String description;
    @Schema(name="菜品ID",description = "菜品ID")
    private int status;
    @Schema(name="菜品创建时间",description = "菜品创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @Schema(name="菜品更新时间",description = "菜品更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @Schema(name="菜品对应的分类ID",description = "菜品对应的分类ID")
    private int dishTypeId;
    @Schema(name="菜品是否首页商家推荐区显示",description = "菜品是否首页商家推荐区域显示")
    private int recommend;
    @Schema(name="菜品库存",description = "菜品库存")
    private int inventory;
}
