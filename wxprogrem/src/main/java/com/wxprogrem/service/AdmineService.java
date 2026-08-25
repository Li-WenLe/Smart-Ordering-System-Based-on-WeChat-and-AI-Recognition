package com.wxprogrem.service;

import com.wxprogrem.pojo.Employee;

public interface AdmineService {
    Employee login(String username, String password);

    Employee getEmployeeByUsername(String username);

    void regist(String username, String password);
}
