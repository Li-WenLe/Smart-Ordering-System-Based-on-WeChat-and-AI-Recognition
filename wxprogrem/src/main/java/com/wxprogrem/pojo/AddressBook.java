package com.wxprogrem.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressBook {
    @Schema(description="AddressBook主键ID")
    private int id;
    @Schema(description="用户ID")
    private int userId;
    @Schema(description="用户手机号")
    private String phone;
    @Schema(description="用户昵称（称呼）")
    private String name;
    @Schema(description="收货地址行政区")
    private String region;
    @Schema(description="收货地址详细地址")
    private String detailAddress;
    @Schema(description="是否为默认收货地址")
    private Boolean isDefaultAddress;
    @Schema(description="收货地址标签")
    private String tag;
}
