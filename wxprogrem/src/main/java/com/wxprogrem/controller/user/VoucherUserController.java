package com.wxprogrem.controller.user;

import com.wxprogrem.pojo.Voucher;
import com.wxprogrem.pojo.VoucherUser;
import com.wxprogrem.service.VoucherService;
import com.wxprogrem.service.VoucherUserService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user/voucher")

@Tag(name = "用户端优惠券相关接口",description = "用户端优惠券相关api")
public class VoucherUserController {
    @Autowired
    private VoucherUserService voucherUserService;
    @Autowired
    private VoucherService voucherService;

    @Operation(summary = "根据用户ID获取相关优惠券信息",description = "根据用户ID获取相关优惠券信息")
    @PostMapping
    public Result<List<VoucherUser>>getAllByUserId(@RequestBody Map<String, Integer> payload ) {
        int userId = payload.get("userId");
        log.info("getAllByUserId:{}", userId);
        List<VoucherUser>list=voucherUserService.getAllByUserId(userId);
        return Result.success(list);
    }
    @Operation(summary = "获取商家发布的所有优惠券信息",description = "获取商家发布的所有优惠券信息")
    @PostMapping("/getall")
    public Result<List<Voucher>> getAll() {
        log.info("调用接口");
        List<Voucher>list=voucherService.getAllVoucher();
        return Result.success(list);
    }

    @Operation(summary = "更新用户使用后的优惠券信息",description = "更新用户使用后的优惠券信息")
    @PostMapping("/use")
    public Result Usedvoucher(@RequestBody VoucherUser voucherUser) {
        int userId=voucherUser.getUserId();
        int voucherId=voucherUser.getVoucherId();
        voucherUserService.Usedvoucher(userId,voucherId);
        return Result.success();

    }

}
