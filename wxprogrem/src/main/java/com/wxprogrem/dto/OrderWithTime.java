package com.wxprogrem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithTime {
    @Schema(description="订单号ID")
    private String orderId;
    @Schema(description="订单的创建时间")
    private LocalDateTime createTime;
}