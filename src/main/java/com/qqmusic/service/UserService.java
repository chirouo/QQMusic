package com.qqmusic.service;

import com.qqmusic.dto.Result;
import com.qqmusic.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author gaoxiang
* @description 针对表【tb_user】的数据库操作Service
* @createDate 2024-01-13 17:21:10
*/
public interface UserService extends IService<User> {

    Result getUserList();

    Result login(User user);

    Result register(String jsonData);

    Result updataUserInfo(String jsonData);

    User searchByAccount(String account);

    Result search(String username);

    Result deleteUsers(String jsonData);
    void removeByAccount(String account);

}
