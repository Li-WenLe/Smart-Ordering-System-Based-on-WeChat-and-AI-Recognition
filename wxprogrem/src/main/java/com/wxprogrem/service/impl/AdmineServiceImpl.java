package com.wxprogrem.service.impl;

import com.wxprogrem.mapper.AdmineMapper;
import com.wxprogrem.pojo.Employee;
import com.wxprogrem.service.AdmineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdmineServiceImpl implements AdmineService {
    @Autowired
    private AdmineMapper admineMapper;
    @Override
    public Employee login(String username, String password) {
         return admineMapper.login(username, password);
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        return admineMapper.getEmployeeByUsername(username);
    }

    @Override
    public void regist(String username, String password) {
        admineMapper.regist(username,password);
    }
}
