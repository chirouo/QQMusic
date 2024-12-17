package com.qqmusic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.FollowList;
import com.qqmusic.entity.User;
import com.qqmusic.service.FollowListService;
import com.qqmusic.mapper.FollowListMapper;
import com.qqmusic.service.UserService;
import com.qqmusic.utils.MyHashUtil;
import com.qqmusic.utils.MySearchUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoxiang
 * @description 针对表【follow_list】的数据库操作Service实现
 * @createDate 2024-01-12 13:22:58
 */
@Service
public class FollowListServiceImpl extends ServiceImpl<FollowListMapper, FollowList>
        implements FollowListService{
    @Resource
    MySearchUtil mySearchUtil;
    @Resource
    UserService userService;

    @Override
    public Result fanFollowHero(String fanAccount, String heroAccount) {
        LambdaQueryWrapper<FollowList> followListWrapper = new LambdaQueryWrapper<>();
        followListWrapper.eq(FollowList::getFollowerId, fanAccount).eq(FollowList::getBefollowedId, heroAccount);
        FollowList followList = new FollowList();
        followList.setFollowerId(fanAccount).setBefollowedId(heroAccount);
        List<FollowList> list = list(followListWrapper);
        if (!list.isEmpty()) return Result.fail("您已经关注了该用户,请勿重复关注!");
        boolean success = save(followList);
        if (success) return Result.ok("关注成功!");
        return Result.fail("关注失败~");
    }

    /**
     * 获得用户的粉丝
     * @param account
     * @return
     */
    @Override
    public Result userFans(String account) {
        User user = mySearchUtil.getUserByAccount(account);
        if(user == null) return Result.fail("抱歉!该用户不存在!");
        //该用户是befollwed_id,查询这个id对应的粉丝
        LambdaQueryWrapper<FollowList> followListWrapper = new LambdaQueryWrapper<>();
        followListWrapper.select(FollowList::getFollowerId).eq(FollowList::getBefollowedId, account);
        List<FollowList> fansList = list(followListWrapper);
        ArrayList<User> fans = new ArrayList<>();
        for (FollowList followList : fansList) {
            //先查询是否存在该粉丝
            String followerId = followList.getFollowerId();
            User fan = userService.searchByAccount(followerId);
            //如果User表中不存在这个用户,先去User表中把对应的用户删除
            if (fan == null){
                userService.removeByAccount(followerId);
                continue;
            }
            fans.add(fan);
        }
        return Result.ok(fans);
    }

    @Override
    public Result userHeros(String account) {
        //查询该用户是否存在
        User user = mySearchUtil.getUserByAccount(account);
        if(user == null) return Result.fail("抱歉!该用户不存在!");
        //该用户是follwer_id,查询这个id对应的heros
        LambdaQueryWrapper<FollowList> followListWrapper = new LambdaQueryWrapper<>();
        followListWrapper.select(FollowList::getFollowerId).eq(FollowList::getFollowerId, account);
        List<FollowList> heroList = list(followListWrapper);
        ArrayList<User> heros = new ArrayList<>();
        for (FollowList hero1 : heroList) {
            //先查询是否存在该hero
            String followerId = hero1.getBefollowedId();
            User hero = userService.searchByAccount(followerId);
            //如果User表中不存在这个用户,先去User表中把对应的用户删除
            if (hero == null){
                userService.removeByAccount(followerId);
                continue;
            }
            heros.add(hero);
        }
        return Result.ok(heros);
    }

    /**
     * 删除
     * @param account
     */

}




