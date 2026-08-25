package com.wxprogrem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetMeal implements Serializable {
    private int id;
    private String name;
    private Double price;
    private int status;
    private String description;
    private String cover;
}
