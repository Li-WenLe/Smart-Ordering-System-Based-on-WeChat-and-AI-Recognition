package com.wxprogrem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SickVoucher {
    private int id;
    private int voucherId;
    private int userId;
    private int account;
}
