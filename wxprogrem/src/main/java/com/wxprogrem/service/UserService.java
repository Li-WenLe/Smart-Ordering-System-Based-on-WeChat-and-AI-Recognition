package com.wxprogrem.service;

import com.wxprogrem.pojo.User;
import org.apache.ibatis.annotations.Insert;

import java.util.List;
import java.util.Map;

public interface UserService {
    User login(String username, String password);

    void add(User user);

    User getUserByUsername(String username);

    List<Map<String, Object>> getWeeklyUserRegistrations();
}
