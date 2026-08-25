package com.wxprogrem.controller.user;

import com.wxprogrem.pojo.Voucher;
import com.wxprogrem.pojo.VoucherUser;
import com.wxprogrem.service.VoucherService;
import com.wxprogrem.service.VoucherUserService;
import com.wxprogrem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user/voucher")
public class VoucherUserController {
    @Autowired
    private VoucherUserService voucherUserService;
    @Autowired
    private VoucherService voucherService;
    @PostMapping
    public Result<List<VoucherUser>>getAllByUserId(@RequestBody Map<String, Integer> payload ) {
        int userId = payload.get("userId");
        log.info("getAllByUserId:{}", userId);
        List<VoucherUser>list=voucherUserService.getAllByUserId(userId);
        return Result.success(list);
    }
    @PostMapping("/getall")
    public Result<List<Voucher>> getAll() {
        log.info("调用接口");
        List<Voucher>list=voucherService.getAllVoucher();
        return Result.success(list);
    }
    @PostMapping("/use")
    public Result Usedvoucher(@RequestBody VoucherUser voucherUser) {
        int userId=voucherUser.getUserId();
        int voucherId=voucherUser.getVoucherId();
        voucherUserService.Usedvoucher(userId,voucherId);
        return Result.success();

    }

}
