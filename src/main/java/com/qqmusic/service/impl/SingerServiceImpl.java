package com.qqmusic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqmusic.entity.Singer;
import com.qqmusic.service.SingerService;
import com.qqmusic.mapper.SingerMapper;
import org.springframework.stereotype.Service;

/**
* @author gaoxiang
* @description 针对表【tb_singer】的数据库操作Service实现
* @createDate 2024-01-12 23:43:33
*/
@Service
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer>
    implements SingerService{

}




