package com.wxprogrem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitmqSickVoucherDTO implements Serializable {
    private Integer voucherId;
    private Integer userId;
}
