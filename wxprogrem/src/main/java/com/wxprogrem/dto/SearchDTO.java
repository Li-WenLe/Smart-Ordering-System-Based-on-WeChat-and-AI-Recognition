package com.wxprogrem.dto;

import com.wxprogrem.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
    private int pageSize;
    private int pageNum;
    private Order order;
}
