package com.wxprogrem.controller.admin;

import com.wxprogrem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController("AdminUserController")
@RequestMapping("/admin/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/weeklyregistrations")
    public List<Map<String, Object>> getWeeklyRegistrations() {
        return userService.getWeeklyUserRegistrations();
    }
}
