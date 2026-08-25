package com.wxprogrem.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Banner {
    @Schema(description = "轮播图ID")
    private int id;
    @Schema(description = "轮播图封面")
    private String cover;
    @Schema(description = "轮播图内容信息")
    private String content;
    @Schema(description = "轮播图是否小程序首页展示")
    private int ishow;
    @Schema(description = "轮播图封面")
    private String image;
    @Schema(description = "轮播图标题信息")
    private String title;
}
