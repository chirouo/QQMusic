package com.qqmusic.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqmusic.dto.MusicDTO;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.SysFile;
import com.qqmusic.entity.User;
import com.qqmusic.service.MusicService;
import com.qqmusic.service.SysFileService;
import com.qqmusic.mapper.SysFileMapper;
import com.qqmusic.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
* @author gaoxiang
* @description 针对表【sys_file】的数据库操作Service实现
* @createDate 2024-01-17 20:27:59
*/
@Service
@Slf4j
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile>
    implements SysFileService{
    @Resource
    MusicService musicService;
    @Resource
    UserService userService;
    //文件磁盘路径
    @Value("${files.upload.imagespath}")
    private  String imagesUploadPath;
    @Value("${files.upload.musicspath}")
    private String musicsUploadPath;
    @Value("${files.upload.lyricspath}")
    private String lyricsUploadPath;
    @Value("${files.upload.coverspath}")
    private String coversUploadPath;
    /**
     *     imageslocatepath: http://123.57.78.66/images/
     *     lyricslocatepath: http://123.57.78.66/lyrics/
     *     musicocatepath: http://123.57.78.66/musics/
     *     coverslocatepath: http://123.57.78.66/covers/
     */
    @Value("${files.upload.musiclocatepath}")
    private String musicsPath;
    @Value("${files.upload.lyricslocatepath}")
    private String lyricsPath;
    @Value("${files.upload.coverslocatepath}")
    private String coversPath;
    @Value("${files.upload.imageslocatepath}")
    private String imagesPath;

    private String fileUploadPath;
    private String fileLocatePath;
    private final String[] CHOOSE = {"music", "lyric", "cover", "image"};
    @Override
    public Result uploadMusicFile(MultipartFile lyricFile, MultipartFile coverFile,
                                  MultipartFile musicFile, MusicDTO musicDTO) {
        String musicUrl;
        String lyricUrl;
        String coverUrl;
        HashMap<String, String> result = new HashMap<>();
        try {
            if(musicFile != null){
                musicUrl = uploadFile(musicFile, CHOOSE[0]);
                musicDTO.setMusicUrl(musicUrl);
                result.put("musicUrl", musicUrl);
            }
            if(lyricFile != null) {
                lyricUrl = uploadFile(lyricFile, CHOOSE[1]);
                musicDTO.setLyricUrl(lyricUrl);
                result.put("lyricUrl", lyricUrl);
            }
            if(coverFile != null){
                coverUrl = uploadFile(coverFile, CHOOSE[2]);
                musicDTO.setCoverUrl(coverUrl);
                result.put("coverUrl", coverUrl);
            }
            //将URL存入result
            ArrayList<MusicDTO> musicDTOS = new ArrayList<>();
            musicDTOS.add(musicDTO);
            musicService.insertMusics(musicDTOS);
            return Result.ok(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result uploadImageFile(MultipartFile imageFile) {
        try {
            String imageUrl = uploadFile(imageFile, CHOOSE[3]);
            HashMap<String, String> result = new HashMap<>();
            result.put("imageUrl", imageUrl);
            return Result.ok(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Result userRegisterWithFile(User user) {
        String account = user.getAccount();
        User userByAccount= userService.searchByAccount(account);
        if (userByAccount != null)
        {
            return Result.fail("用户 " + user.getUsername()+ " 账号已存在!");
        }
        if (user.getImageFile() != null){
            try {
                String avatUrl = uploadFile(user.getImageFile(), CHOOSE[3]);
                user.setAvatarUrl(avatUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        //将用户的密码用MD5加密
        user.setPassword(SecureUtil.md5(user.getPassword()));
        userService.save(user);
        return Result.ok("注册成功!");
    }
    public String uploadFile(MultipartFile file, String choose) throws IOException {

        //文件存储的磁盘
        switch (choose){
            case "music":
                fileUploadPath=musicsUploadPath;
                fileLocatePath=musicsPath;
                break;
            case "lyric":
                fileUploadPath=lyricsUploadPath;
                fileLocatePath=lyricsPath;
                break;
            case "cover":
                fileUploadPath=coversUploadPath;
                fileLocatePath=coversPath;
                break;
            case "image":
                fileUploadPath=imagesUploadPath;
                fileLocatePath=imagesPath;
                break;
        }
        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        //获取文件的类型
        String type = FileUtil.extName(originalFilename);
        log.info("文件类型是：" + type);
        //获取文件大小
        long size = file.getSize();

        File uploadParentFile = new File(fileUploadPath);
        //判断文件目录是否存在
        if(!uploadParentFile.exists()) {
            //如果不存在就创建文件夹
            uploadParentFile.mkdirs();
        }

        //定义一个文件唯一标识码（UUID）
        String uuid = UUID.randomUUID().toString();
        String fileUUID = uuid + StrUtil.DOT + type;
        //new对象的时候要初始化文件上传时候的名字
        File uploadFile = new File(fileUploadPath + fileUUID);

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        SysFile dbFile = this.getFileByMd5(md5);
        if (dbFile != null) { // 文件已存在
            url = dbFile.getUrl();
        } else {
            // 将接受到的文件上传到磁盘的文件中
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = fileLocatePath + fileUUID;
            //存储至数据库
            SysFile saveFile = new SysFile();
            saveFile.setName(originalFilename);
            saveFile.setType(type);
            saveFile.setSize(size/1024);//转成kb
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            save(saveFile);
        }
        return url;
    }

    /**
     * 防止文件重复上传
     * @param md5
     * @return
     */
    public SysFile getFileByMd5(String md5) {
        LambdaQueryWrapper<SysFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFile::getMd5, md5);
        List<SysFile> list = this.list(queryWrapper);
        return list.isEmpty() ? null : list.get(0);
    }
}




