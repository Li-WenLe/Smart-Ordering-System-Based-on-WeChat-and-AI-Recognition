package com.wxprogrem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail implements Serializable {
    private int id;
    private String dishname;
    private String image;
    private String orderId;
    private int dishId;
    //商品数量
    private int number;
    //商品单价
    private double acount;
}
