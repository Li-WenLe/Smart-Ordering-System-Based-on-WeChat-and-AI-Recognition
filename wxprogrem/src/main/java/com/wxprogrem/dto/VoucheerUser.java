package com.wxprogrem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucheerUser {
    private int id;
    private Integer voucheerId;
    private Integer acount;
    private Integer userId;
}
