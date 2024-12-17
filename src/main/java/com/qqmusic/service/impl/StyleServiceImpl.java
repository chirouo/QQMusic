package com.qqmusic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.*;
import com.qqmusic.service.MusicService;
import com.qqmusic.service.SonglistMusicService;
import com.qqmusic.service.StyleService;
import com.qqmusic.mapper.StyleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author gaoxiang
* @description 针对表【tb_style】的数据库操作Service实现
* @createDate 2024-01-12 13:22:16
*/
@Service
public class StyleServiceImpl extends ServiceImpl<StyleMapper, Style>
    implements StyleService{

    @Override
    public Result showStyleList() {
        //展示之前查看这个风格有没有音乐了
        List<Style> list = list();
        return Result.ok(list);
    }
}




