package com.qqmusic.utils;

import cn.hutool.core.util.HashUtil;
import com.qqmusic.dto.MusicDTO;
import com.qqmusic.entity.Music;
import org.springframework.stereotype.Component;
@Component
public class MyHashUtil {
    public String musicDTOHash(MusicDTO musicDTO) {
//        String musicname = musicDTO.getMusicname();
//        String singername = musicDTO.getSingername();
//        String
//        //拼接字符串
//        String concat = musicname.concat(singername.toString());
//        int i = HashUtil.jsHash(concat);
//
//        String hashString = Integer.toString(i);
        int i = musicDTO.hashCode();
        String hashString = Integer.toString(i);
        return hashString;
    }
    public String musicHash(Music music) {
        int i = music.hashCode();
        String hashString = Integer.toString(i);
        return hashString;
    }
}
