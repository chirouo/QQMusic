package com.qqmusic.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qqmusic.entity.User;
import com.qqmusic.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class MySearchUtil {
    @Resource
    UserService userService;
    public  User getUserByAccount(String account)
    {
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<User>();
        userWrapper.eq(User::getAccount, account);
        User user = userService.getOne(userWrapper);
        return user;
    }
}
