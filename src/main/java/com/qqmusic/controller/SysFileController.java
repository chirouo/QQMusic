package com.qqmusic.controller;

import com.qqmusic.dto.MusicDTO;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.User;
import com.qqmusic.service.SysFileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author hj
 */
@RestController
@RequestMapping("/file")
public class SysFileController {
    @Resource
    SysFileService sysFileService;
    @PostMapping("/upload/musicFile")
    //必须要用DTO将三个文件封装在一起,而且@RequestBody接不到三个文件,必须直接接文参数
    public Result uploadMusicFile(MusicDTO musicDTO)throws IOException {
        return sysFileService.uploadMusicFile(musicDTO.getLyricFile(), musicDTO.getCoverFile(), musicDTO.getMusicFile(),musicDTO);
    }
    @PostMapping("/upload/imageFile")
    public Result uploadImageFile(@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        return sysFileService.uploadImageFile(imageFile);
    }
    @PostMapping("/upload/registerWithFile")
    public Result userRegisterWithFile(User user)
    {
        return sysFileService.userRegisterWithFile(user);
    }
}

