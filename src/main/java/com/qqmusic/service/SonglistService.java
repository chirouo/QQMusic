package com.qqmusic.service;

import com.qqmusic.dto.Result;
import com.qqmusic.entity.Songlist;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author gaoxiang
* @description 针对表【songlist】的数据库操作Service
* @createDate 2024-01-16 16:05:47
*/
public interface SonglistService extends IService<Songlist> {

    Result addSongList(String songlistname, String account);

    Result deleteSongLists(String jsonData);

    Result addMusicToSongList(String jsonData);

    Result showMusicsById(Integer songListId);

    Result showUserSongListByAccount(String account);

    Result showUserSongListByName(String userName);

    Result getAll();

    Result showSongListByName(String songlistName);

    Result bestMusicOfUser(String account, Integer isReverse);

    Result bestMusics(Integer isReverse);

    Result updateSonglist(Songlist songlist);
}
