package com.wxprogrem.controller.user;

import com.wxprogrem.pojo.AddressBook;
import com.wxprogrem.service.AddressService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController("userAddressController")
@RequestMapping("/user/address")

@Tag(name="用户端地址管理",description = "用户端地址管理相关api")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Operation(summary = "添加收货地址信息",description = "添加收货地址信息")
    @PostMapping("/add")
    public Result add(@RequestBody AddressBook addressBook) {
        int userId=addressBook.getId();
        log.info("添加收货地址信息:地址信息：{}，用户Id:{}", addressBook,userId);
        if(!addressBook.getIsDefaultAddress()) {
            //修改所有的isdefault信息为1，保证默认地址的唯一性
            addressService.update(userId);
        }
        addressService.add(addressBook);
        return Result.success();
    }


    @Operation(summary = "根据用户ID查找收默认收货地址信息",description = "根据用户ID查找收默认收货地址信息")//获取默认地址
    @GetMapping("/get")
    public Result get(@RequestParam int id) {
        log.info("根据用户ID查找收默认收货地址信息--userId:{}", id);
        AddressBook addressBook=addressService.getById(id);
        return Result.success(addressBook);
    }


    @Operation(summary = "根据用户ID查找所有收货地址信息",description = "根据用户ID查找所有收货地址信息")//获取默认地址
    @GetMapping("/getall")
    public Result<List<AddressBook>> getAll(@RequestParam int id) {
        log.info("根据用户ID查找所有收货地址信息--userId:{}", id);
        List<AddressBook>list=addressService.getAll(id);
        return Result.success(list);
    }

    @Operation(summary = "根据用户ID查找收默认收货地址信息",description = "根据用户ID查找收默认收货地址信息")//获取默认地址
    @GetMapping("/getbyid")
    public Result<AddressBook> getById(@RequestParam int id) {
        log.info("根据用户ID查找收默认收货地址信息--userId:{}", id);
        AddressBook addressBook=addressService.getinfoById(id);
        return Result.success(addressBook);
    }

    @Operation(summary = "修改用户收货地址信息",description = "修改用户收货地址信息")//获取默认地址
    @PostMapping("/updateinfo")
    public Result update(@RequestBody AddressBook addressBook) {
        int userId=addressBook.getId();
        log.info("修改用户收货地址信息--updated addressBook:{},userId:{}", addressBook,userId);
        addressService.updateInfo(addressBook);
        return Result.success();
    }
}
