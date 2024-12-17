package com.qqmusic.service;

import com.qqmusic.dto.MusicDTO;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.SysFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qqmusic.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author gaoxiang
* @description 针对表【sys_file】的数据库操作Service
* @createDate 2024-01-17 20:27:59
*/
public interface SysFileService extends IService<SysFile> {
    //文件上传路径


    Result uploadMusicFile(MultipartFile lyricFile, MultipartFile coverFile, MultipartFile musicFile, MusicDTO musicDTO);

    Result uploadImageFile(MultipartFile imageFile);
    Result userRegisterWithFile(User user);
}
