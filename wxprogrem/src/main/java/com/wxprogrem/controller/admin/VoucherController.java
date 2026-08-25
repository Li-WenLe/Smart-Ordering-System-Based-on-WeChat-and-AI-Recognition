package com.wxprogrem.controller.admin;

import com.wxprogrem.pojo.Voucher;
import com.wxprogrem.service.VoucherService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/voucher")
@Tag(name="修改优惠券相关信息",description = "修改优惠券相关信息相关api")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @Operation(summary = "获取所有的优惠券信息",description = "获取所有的优惠券信息")
    @GetMapping()
    public Result<List<Voucher>> getAllVoucher(){
        List<Voucher>list=voucherService.getAllVoucher();
        return Result.success(list);
    }

    @Operation(summary = "修改优惠券信息",description = "修改优惠券信息")
    @PostMapping("/update")
    public Result updateVoucher(@RequestBody Voucher voucher){
        log.info("前端传递的参数:{}",voucher);
        voucherService.updateVoucher(voucher);
        return Result.success();
    }

    @Operation(summary = "添加优惠券信息",description = "添加优惠券信息")
    @PostMapping("/add")
    public Result addVoucher(@RequestBody Voucher voucher){
        log.info("前端传递的参数:{}",voucher);
        voucherService.addVoucher(voucher);
        return Result.success();
    }

    @Operation(summary = "删除优惠券信息",description = "删除优惠券信息")
    @PostMapping("/delete")
    public Result deleteVoucher(@RequestParam int id){
        log.info("前端传递参数：{}",id);
        voucherService.deleteVoucher(id);
        return Result.success();
    }
}
