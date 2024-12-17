package com.qqmusic.controller;

import com.qqmusic.dto.Result;
import com.qqmusic.service.FollowListService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/followlist")
@RestController
public class FollowListController {
    @Resource
    FollowListService followListService;
    @GetMapping("/userFans/{account}")
    public Result userFans(@PathVariable("account") String account)
    {
        return followListService.userFans(account);
    }
    @GetMapping("/userHeros/{account}")
    public Result userHeros(@PathVariable("account") String account){
        return followListService.userHeros(account);
    }
    @PostMapping("/follow/{fanAccount}/{heroAccount}")
    public Result fanFollowHero(@PathVariable("fanAccount") String fanAccount, @PathVariable("heroAccount") String heroAccount)
    {
        return followListService.fanFollowHero(fanAccount, heroAccount);
    }
}
