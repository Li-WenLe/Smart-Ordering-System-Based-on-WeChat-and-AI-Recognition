package com.wxprogrem.dto;

import com.wxprogrem.pojo.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    private String orderId;
    private List<OrderDetail> items;
    private double totalPrice;
    private int status;
    private LocalDateTime orderTime; // 新增字段
}