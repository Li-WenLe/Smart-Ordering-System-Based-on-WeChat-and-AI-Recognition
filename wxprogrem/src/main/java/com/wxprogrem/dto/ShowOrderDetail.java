package com.wxprogrem.dto;

import com.wxprogrem.pojo.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowOrderDetail {
    private int dishId;
    private String dishname;
    private String image;
    private int totalSales;  // 总销售数量
    private double totalAmount;  // 总销售额
}
