package com.qqmusic.service;

import com.qqmusic.dto.MusicDTO;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.Music;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author gaoxiang
 * @description 针对表【tb_music】的数据库操作Service
 * @createDate 2024-01-13 00:08:51
 */
public interface MusicService extends IService<Music> {

    Result searchMuscic(String musicname);

    Result getMusicList();

    Result delMusics(List<String> musicIds);

    Result insertMusics(List<MusicDTO> list);

    MusicDTO searchById(String musicId);

    Result updateByIds(String jsonData);

    Result searchByStyle(String styleName);
    Result searchBySinger(String singerName);
    List<MusicDTO> musicToDTo(List<Music> musicList);
    Music searchByMusicId(String musicId);
}