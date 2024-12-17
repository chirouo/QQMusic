package com.qqmusic.service;

import com.qqmusic.dto.Result;
import com.qqmusic.entity.Style;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author gaoxiang
* @description 针对表【tb_style】的数据库操作Service
* @createDate 2024-01-12 13:22:16
*/
public interface StyleService extends IService<Style> {


    Result showStyleList();
}
