package com.wxprogrem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetMealDish {
    private int id;
    private String name;
    private int setmealId;
    private String cover;
    private Double price;
    private int acount;
}
