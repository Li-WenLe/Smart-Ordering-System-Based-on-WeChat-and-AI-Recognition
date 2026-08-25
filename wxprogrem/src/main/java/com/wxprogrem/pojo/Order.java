package com.wxprogrem.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int id;
    private String name;
    private String phone;
    private double total;
    private int userId;
    private String address;
    private int status;
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String orderId;
    private int payedTotal;
}
