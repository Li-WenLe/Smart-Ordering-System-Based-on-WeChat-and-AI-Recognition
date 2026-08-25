package com.wxprogrem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishType {
    private int id;
    private int dishId;
    private String type;
    private int ishow;
    private String cover;
    private int dishTypeId;
}
