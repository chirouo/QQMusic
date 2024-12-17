package com.qqmusic.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.User;
import com.qqmusic.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/list")
    public Result getUserList() {
        return userService.getUserList();
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user)
    {
        return userService.login(user);
    }
    @PostMapping("/register")
    public Result register(@RequestBody String jsonData)
    {
        return userService.register(jsonData);
    }
    @GetMapping("/search")
    public Result search(@RequestParam("username") String username)
    {
        return userService.search(username);
    }
    @PutMapping()
    public Result updateUserInfo(@RequestBody String jsonData)
    {
        return userService.updataUserInfo(jsonData);
    }
    @GetMapping("/{account}")
    public Result searchByAccount(@PathVariable("account") String account)
    {
        User user = userService.searchByAccount(account);
        if (user == null) return Result.fail("用户不存在!请确认账号是否正确");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        return Result.ok(users);
    }
    @PostMapping("/deleteUsers")
    public Result deleteUsers(@RequestBody String jsonData)
    {
        return userService.deleteUsers(jsonData);
    }
}
