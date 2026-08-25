package com.wxprogrem.dto;

import com.wxprogrem.pojo.Dish;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowOrderDetail {
    @Schema(description="商品主键ID")
    private int dishId;
    @Schema(description="商品名")
    private String dishname;
    @Schema(description="商品封面图")
    private String image;
    @Schema(description="商品总价")
    private int totalSales;  // 总销售数量
    @Schema(description="商品数量")
    private double totalAmount;  // 总销售额
}
