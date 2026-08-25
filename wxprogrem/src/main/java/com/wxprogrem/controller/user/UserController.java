package com.wxprogrem.controller.user;
import com.wxprogrem.mapper.UserMapper;
import com.wxprogrem.pojo.User;
import com.wxprogrem.service.UserService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")

@Tag(name = "用户端用户登录注册相关接口",description = "用户端用户登录注册相关api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper voucherMapper;

    @Operation(summary = "用户端登录",description = "用户端登录")
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("前端传递参数：{}", user);
        String username = user.getUsername();
        String password = user.getPassword();
        User loginuser= (User) userService.login(username,password);
        if(loginuser != null) {
            return Result.success(loginuser);
        } else {
          return  Result.error("用户名或密码错误");
        }
    }

    @Operation(summary = "用户端注册",description = "用户端注册")
    @PostMapping("/regist")
    public Result regist(@RequestBody User user) {
        log.info("注册参数：{}", user);
        String username=user.getUsername();
        log.info("用户名：{}", username);
        User registUser=userService.getUserByUsername(username);
        if(registUser != null) {
            return Result.error("用户名被占用");
        }
        String password=user.getPassword();
        String phone=user.getPhone();
        userService.add(user);
        return Result.success();
    }
}
