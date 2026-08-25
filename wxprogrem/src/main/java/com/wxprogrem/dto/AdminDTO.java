package com.wxprogrem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AdminDTO {
    @Schema(description="管理员用户主键ID")
    private int id;
    @Schema(description="管理员用户账号名主键ID")
    private String name;
}
