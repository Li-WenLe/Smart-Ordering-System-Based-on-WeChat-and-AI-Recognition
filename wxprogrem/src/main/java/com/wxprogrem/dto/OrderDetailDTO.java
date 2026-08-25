package com.wxprogrem.dto;

import com.wxprogrem.pojo.OrderDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @Schema(description="订单ID号")
    private String orderId;
    @Schema(description="订单包含的商品信息列表")
    private List<OrderDetail> items;
    @Schema(description="订单总价")
    private double totalPrice;
    @Schema(description="订单状态")
    private int status;
    @Schema(description="订单的下单时间")
    private LocalDateTime orderTime; // 新增字段
}