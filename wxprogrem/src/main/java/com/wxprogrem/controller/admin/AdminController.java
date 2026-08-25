package com.wxprogrem.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.wxprogrem.dto.AdminDTO;
import com.wxprogrem.pojo.Employee;
import com.wxprogrem.service.AdmineService;
import com.wxprogrem.utils.Jwtutils;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin")
@Slf4j
@Tag(name="商家管理员",description = "商家管理员相关api")
public class AdminController {
    @Autowired
    private AdmineService admineService;

    @Operation(summary = "商家管理员登录",description = "商家管理员管理页面登录")
    @PostMapping("/login")
    public Result login(@RequestParam(required = true) String username, @RequestParam String password) {
        Employee emp = admineService.getEmployeeByUsername(username);
        if (emp == null) {
            return Result.error("用户名错误");
        }
        if (!emp.getPassword().equals(password)) {
            return Result.error("密码错误");
        }
        //封装传回给前端的数据，防止关键信息泄露
        AdminDTO adminDTO = BeanUtil.copyProperties(emp, AdminDTO.class);
        //用map封装信息
        Map<String, Object> adminMap = new HashMap<>();
        adminMap.put("username", emp.getUsername());
        adminMap.put("id", emp.getId());
        //形成token密钥并返回给前端
        String token = Jwtutils.getToken(adminMap);
        return Result.success(token);
    }

    @Operation(summary = "商家管理员注册",description = "商家管理员管理页面注册")
    @PostMapping("/regist")
    public Result register(@RequestParam String username,@RequestParam String password) {
        //判断是否存在重名用户
        Employee u=admineService.getEmployeeByUsername(username);
        if(u==null) {
            admineService.regist(username,password);
            return Result.success("注册成功");
        }else {
            return Result.error("用户名已经被占用");
        }
    }
}
