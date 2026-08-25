package com.wxprogrem.service.impl;

import com.wxprogrem.mapper.SetMealMapper;
import com.wxprogrem.mapper.UserMapper;
import com.wxprogrem.mapper.VoucherMapper;
import com.wxprogrem.mapper.VoucherUserMapper;
import com.wxprogrem.pojo.User;
import com.wxprogrem.pojo.VoucherUser;
import com.wxprogrem.service.SetMealService;
import com.wxprogrem.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMappere;
    @Autowired
    private VoucherMapper voucherMappere;
    @Autowired
    private VoucherUserMapper voucherUserMappere;
    @Override
    public User login(String username, String password) {
        return userMappere.selectByUserNameAndPassword(username,password);
    }

    @Override
    @Transactional
    public void add(User user) {
        User newUser = new User();
        BeanUtils.copyProperties(user,newUser);
        newUser.setRegisterTime(LocalDateTime.now());
        newUser.setPhoto("https://img.ixintu.com/download/jpg/20200910/f9256155491e54bf5e99bf29eece0156_512_512.jpg!ys");
        //获取10元优惠券对应的id
        int voucehrId=3;//voucherMappere.getVoucher();
        //插入数据
        userMappere.add(newUser);
        User register = userMappere.selectByUserNameAndPassword(newUser.getUsername(),newUser.getPassword());
        int userId=register.getId();
        int acount=1;
        voucherUserMappere.insert(userId,voucehrId,acount);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMappere.getUserByUsername(username);
    }

    @Override
    public List<Map<String, Object>> getWeeklyUserRegistrations() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6); // 最近7天(包括今天)

        return userMappere.countDailyRegistrations(startDate, endDate);
    }
}
