package com.qqmusic.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.User;
import com.qqmusic.service.SysFileService;
import com.qqmusic.service.UserService;
import com.qqmusic.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoxiang
 * @description 针对表【tb_user】的数据库操作Service实现* @createDate 2024-01-12 13:22:23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{
    /**
     * 获得用户信息列表
     * @return
     */
    @Override
    public Result getUserList() {
        List<User> userList = list();
        return Result.ok(userList);
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public Result login(User user) {
//        System.out.println(user);
        String account = user.getAccount();
        String password = user.getPassword();
        //password用MD5加密进入的数据库,保护个人隐私,因此这里需要用MD5加密后的password去比较
        password = SecureUtil.md5(password);

        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getAccount, account);
        User getByAccount = getOne(userWrapper);
//        System.out.println("账号:" + account);
//        System.out.println("密码:" + password);
        if (getByAccount == null) return Result.fail("用户不存在");
        if (!password.equals(getByAccount.getPassword())) return Result.fail("密码错误");
        return Result.ok(getByAccount);
    }

    /**
     * 用户注册
     * @param jsonData
     * @return
     */
    @Override
    public Result register(String jsonData) {
        JSONObject entries = JSONUtil.parseObj(jsonData);
        List<User> users = entries.getBeanList("users", User.class);
        for (User user : users) {
            String account = user.getAccount();
            User userByAccount= searchByAccount(account);
            if (userByAccount != null)
            {
                return Result.fail("用户 " + user.getUsername()+ " 账号已存在!");
            }
            //将用户的密码用MD5加密
            user.setPassword(SecureUtil.md5(user.getPassword()));
            save(user);
        }
        return Result.ok("注册成功!");
    }


    /**
     * 模糊搜索用户
     * @param username
     * @return
     */
    @Override
    public Result search(String username) {
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.like(User::getUsername, username);
        List<User> userList = list(userWrapper);
        return Result.ok(userList);
    }

    /**
     * 删除用户(支持批量删除)
     * @param jsonData
     * @return
     */
    @Override
    public Result deleteUsers(String jsonData) {
        JSONObject entries = JSONUtil.parseObj(jsonData);
        List<String> accounts = entries.getBeanList("accounts", String.class);
        accounts.forEach(this::removeByAccount);
        return Result.ok("删除成功");
    }

    /**
     * 更新用户信息
     * @param jsonData
     * @return
     */
    @Override
    public Result updataUserInfo(String jsonData) {
        JSONObject entries = JSONUtil.parseObj(jsonData);
        List<User> users = entries.getBeanList("users", User.class);
        User user = users.get(0);
        String account = user.getAccount();
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getAccount, account);
        boolean success = update(user, userWrapper);
        if (success) return Result.ok("更新成功");
        return Result.fail("更新失败");
    }

    /**
     * 精准搜索用户(通过账号)
     * @param account
     * @return
     */
    @Override
    public User searchByAccount(String account) {
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getAccount, account);
        User user = getOne(userWrapper);
        if (user == null) {
            return null;
        }
        return user;
    }
    @Override
    public void removeByAccount(String account) {
        //这样写性能高点,就不用searchByAccount了
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getAccount, account);
        remove(userWrapper);
    }
}




