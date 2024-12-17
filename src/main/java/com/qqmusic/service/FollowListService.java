package com.qqmusic.service;

import com.qqmusic.dto.Result;
import com.qqmusic.entity.FollowList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author gaoxiang
* @description 针对表【follow_list】的数据库操作Service
* @createDate 2024-01-15 14:25:19
*/
public interface FollowListService extends IService<FollowList> {

    Result userFans(String account);

    Result userHeros(String account);

    Result fanFollowHero(String fanAccount, String heroAccount);
}
