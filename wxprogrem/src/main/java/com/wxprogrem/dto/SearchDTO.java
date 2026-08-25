package com.wxprogrem.dto;

import com.wxprogrem.pojo.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
    @Schema(description="分页查询每页显示信息量")
    private int pageSize;
    @Schema(description="分页查询起始页码")
    private int pageNum;
    @Schema(description="订单信息")
    private Order order;
}
