package com.wxprogrem.controller.user;

import com.wxprogrem.pojo.VoucherUser;
import com.wxprogrem.service.VoucherService;
import com.wxprogrem.service.VoucherUserService;
import com.wxprogrem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController("/user/voucher")
@RequestMapping("/voucher")
public class VoucherController {
//    @Autowired
//    private VoucherUserService voucherUserService;
//
//    @GetMapping
//    public Result<List<VoucherUser>> getAllByUserId(@RequestParam int userId) {
//        List<VoucherUser> voucherList = voucherUserService.getAllByUserId(userId);
//        return Result.success(voucherList);
//    }

}
