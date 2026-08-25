package com.wxprogrem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitmqSickVoucherDTO implements Serializable {
    @Schema(description="优惠券ID")
    private Integer voucherId;
    @Schema(description="用户ID")
    private Integer userId;
}
