package com.qqmusic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.HashUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqmusic.dto.MusicDTO;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.*;
import com.qqmusic.service.*;
import com.qqmusic.mapper.MusicMapper;
import com.qqmusic.utils.MyHashUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gaoxiang
 * @description 针对表【tb_music】的数据库操作Service实现
 * @createDate 2024-01-12 13:20:45
 */
@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music>
        implements MusicService{



    @Resource
    LanguageService LanguageService;
    @Resource
    StyleService styleService;
    @Resource
    SingerService singerService;
    @Resource
    MyHashUtil myHashUtil;
    @Resource
    LanguageService languageService;
    /**
     * 获得所有音乐列表
     * @return
     */
    @Override
    public Music searchByMusicId(String musicId) {
        LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
        musicWrapper.eq(Music::getMusicId, musicId);
        Music music = getOne(musicWrapper);
        if (music == null) {
            return null;
        }
        return music;
    }
    @Override
    public Result getMusicList() {
        List<Music> musicList = list();
        //删除不需要的style
        LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
        List<Style> styleList = styleService.list();
        for (Style style : styleList) {
            //每次查询之前清空查询wrapper
            musicWrapper.clear();
            Integer styleId = style.getStyleId();
            musicWrapper.eq(Music::getStyleId, styleId);
            List<Music> musicList2 = list(musicWrapper);
            if(musicList2.isEmpty()) removeById(styleId);
        }
        //删除不需要的language
        List<Language> languageList = languageService.list();
        for (Language language : languageList) {
            //每次查询之前清空查询wrapper
            musicWrapper.clear();
            Integer languageId = language.getLanguageId();
            musicWrapper.eq(Music::getLanguageId, languageId);
            List<Music> musicList2 = list(musicWrapper);
            if(musicList2.isEmpty()) removeById(languageId);
        }
        //删除不需要的歌手
        List<Singer> singerList = singerService.list();
        for (Singer singer : singerList) {
            //每次查询之前清空查询wrapper
            musicWrapper.clear();
            Integer singerId = singer.getSingerId();
            musicWrapper.eq(Music::getSingerId, singerId);
            List<Music> musicList2 = list(musicWrapper);
            if(musicList2.isEmpty()) removeById(singerId);
        }
        List<MusicDTO>musicDTOList = musicToDTo(musicList);
        return Result.ok(musicDTOList);
    }

    /**
     * 删除音乐(支持批量删除)
     * @param musicIds
     * @return
     */
    @Transactional
    @Override
    public Result delMusics(List<String> musicIds) {
        for (String musicId : musicIds) {
            LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
            musicWrapper.eq(Music::getMusicId, musicId);
            remove(musicWrapper);
        }
        return Result.ok("删除成功!");
    }

    public Integer insertStyle(MusicDTO music) {
        Style style = null;
        String styleName = music.getStyle();
        if (!styleName.isEmpty()) {//如果传的是空字符串
            //新增风格
            //联表查询, 查询是否有这个风格, 没有就要新增
            LambdaQueryWrapper<Style> styleWrapper = new LambdaQueryWrapper<>();
            styleWrapper.eq(Style::getStyle, styleName);
            style = styleService.getOne(styleWrapper);
            //没有该风格, 新增
            if (style == null) {
                style = new Style();
                style.setStyle(styleName);
                styleService.save(style);
            }
        }
        return style.getStyleId();
    }
    public Integer insertLanguage(MusicDTO music) {
        Language language = null;
        String languagename = music.getLanguagename();
        if (!languagename.isEmpty()) {//如果传的是空字符串
            //新增语言
            //联表查询, 查询是否有这个语言, 没有就要新增
            LambdaQueryWrapper<Language> languageWrapper = new LambdaQueryWrapper<>();
            languageWrapper.eq(Language::getLanguagename, languagename);
            language = languageService.getOne(languageWrapper);
            //没有该风格, 新增
            if (language == null) {
                language = new Language();
                language.setLanguagename(languagename);
                languageService.save(language);
            }
        }
        return language.getLanguageId();
    }
    public Integer insertSinger(MusicDTO music) {
        Singer singer = null;
        String singerName = music.getSingername();
        if (!singerName.isEmpty()) {//如果传的是空字符串
            //新增语言
            //联表查询, 查询是否有这个语言, 没有就要新增
            LambdaQueryWrapper<Singer> singerLambdaQueryWrapper = new LambdaQueryWrapper<>();
            singerLambdaQueryWrapper.eq(Singer::getName, singerName);
            singer = singerService.getOne(singerLambdaQueryWrapper);
            //没有该风格, 新增
            if (singer == null) {
                singer = new Singer();
                singer.setName(singerName);
                singerService.save(singer);
            }
        }
        return singer.getSingerId();
    }

    /**
     * 增加音乐(支持批量增加)
     * @param musicDTOlist
     * @return
     */
    @Transactional
    @Override
    public Result insertMusics(List<MusicDTO> musicDTOlist) {

        for (MusicDTO music : musicDTOlist) {
            //查看该歌曲是否插入过
            //hash计算musicId
            if (music.getSingername().isEmpty())
                return Result.fail("添加的音乐名不能为空!");
            if (music.getMusicname().isEmpty()) {
                return Result.fail("歌手名不能为空!");
            }
            String musicId = myHashUtil.musicDTOHash(music);
            LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
            musicWrapper.eq(Music::getMusicId, musicId);
            Music musicById = getOne(musicWrapper);
            //如果存在该音乐,就不添加
            if (musicById != null) continue;
            //设置musicId
            music.setMusicId(musicId);
            //查看要添加的风格是否存在,不存在就新增一个风格并初始化好
            music.setStyleId(insertStyle(music));
            //查看要添加的语言是否存在
            music.setLanguageId(insertLanguage(music));
            //查看要添加的歌手是否存在
            music.setSingerId(insertSinger(music));
            //转化
            Music toInsertMusic = BeanUtil.copyProperties(music, Music.class);
            save(toInsertMusic);
        }
        return Result.ok("添加音乐成功");
    }

    /**
     * 搜索音乐(精准搜索)
     * @param musicId
     * @return
     */
    @Override
    public MusicDTO searchById(String musicId) {
        LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
        musicWrapper.clear();
        musicWrapper.eq(Music::getMusicId, musicId);
        Music musicById = getOne(musicWrapper);
        MusicDTO musicDTO = BeanUtil.copyProperties(musicById, MusicDTO.class);

        // 查询singer name
        if (musicDTO.getSingerId() != null) {
            Singer singer = singerService.getById(musicDTO.getSingerId());
            String singerName = singer.getName();
            musicDTO.setSingername(singerName);
        }

        // 查询style name
        if (musicDTO.getStyleId() != null) {
            Style style = styleService.getById(musicDTO.getStyleId());
            String styleName = style.getStyle();
            System.out.println("这是style" + styleName);
            musicDTO.setStyle(styleName);
        }

        // 查询language name
        if (musicDTO.getLanguageId() != null) {
            Language language = LanguageService.getById(musicDTO.getLanguageId());
            String languageName = language.getLanguagename();
            musicDTO.setLanguagename(languageName);
        }
        return musicDTO;
    }

    /**
     * 更新音乐信息(支持批量更新)
     * @param jsonData
     * @return
     */
    @Override
    public Result updateByIds(String jsonData) {
        JSONObject entries = JSONUtil.parseObj(jsonData);
        boolean flag = false;
        List<MusicDTO> toUpdateMusics = entries.getBeanList("musics", MusicDTO.class);
        ArrayList<String> toDelmusicIds = new ArrayList<>();
        toUpdateMusics.forEach(music -> {
            //查找这musicId对应的音乐
            String musicId = music.getMusicId();
            LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
            musicWrapper.eq(Music::getMusicId, musicId);
            Music one = getOne(musicWrapper);
            if (one == null ) return;
            //不为空则去删除
            toDelmusicIds.clear();
            toDelmusicIds.add(one.getMusicId());
            delMusics(toDelmusicIds);
            //重新计算它的musicId,并设置新的musicId
            String newMusicId = myHashUtil.musicDTOHash(music);
            music.setMusicId(newMusicId);
            System.out.println(music);
            System.out.println(music.getMusicId() + "这是music为了debug---------");
        });
        //删除后记得新增这首歌
        this.insertMusics(toUpdateMusics);
        return Result.ok("更新成功!");
    }

    @Override
    public Result searchByStyle(String styleName) {
        LambdaQueryWrapper<Style> styleWrapper = new LambdaQueryWrapper<>();
        styleWrapper.eq(Style::getStyle, styleName);
        Style style = styleService.getOne(styleWrapper);
        if (style == null) return Result.fail("不存在该风格的歌曲!");
        Integer styleId = style.getStyleId();
        LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
        musicWrapper.eq(Music::getStyleId, styleId);
        List<Music> musicList = list(musicWrapper);
        List<MusicDTO> musicDTOS = musicToDTo(musicList);
        return Result.ok(musicDTOS);
    }

    @Override
    public Result searchMuscic(String musicname) {
        LambdaQueryWrapper<Music> musicQueryWrapper = new LambdaQueryWrapper<>();
        musicQueryWrapper.like(Music::getMusicname, musicname);
        List<Music> musicList = list(musicQueryWrapper);
        List<MusicDTO> musicDTOS = musicToDTo(musicList);
        return Result.ok(musicDTOS);
    }
    @Override
    public Result searchBySinger(String singerName) {
        //查询该歌手是否存在
        LambdaQueryWrapper<Singer> singerWrapper = new LambdaQueryWrapper<>();
        singerWrapper.eq(Singer::getName, singerName);
        Singer singer = singerService.getOne(singerWrapper);
        if (singer == null) return Result.fail("不存在该歌手!");
        //查询该歌手的歌曲
        Integer singerId = singer.getSingerId();
        LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
        musicWrapper.eq(Music::getSingerId, singerId);
        List<Music> musicList = list(musicWrapper);
        List<MusicDTO> musicDTOList = musicToDTo(musicList);
        return Result.ok(musicDTOList);
    }

    @Override
    public List<MusicDTO> musicToDTo(List<Music> musicList) {
        List< MusicDTO > musicDTOList = BeanUtil.copyToList(musicList, MusicDTO.class);
        //singer
        musicDTOList.stream().forEach(music -> {
            System.out.println(music);
            // 查询singer name
            if (music.getSingerId() == null) return;
            Singer singer = singerService.getById(music.getSingerId());
            String singerName = singer.getName();
            music.setSingername(singerName);

            // 查询style name
            if (music.getStyleId() == null) return;
            Style style = styleService.getById(music.getStyleId());
            String styleName = style.getStyle();
            System.out.println("这是style" + styleName);
            music.setStyle(styleName);

            // 查询language name
            if (music.getLanguageId() == null) return;
            Language language = LanguageService.getById(music.getLanguageId());
            String languageName = language.getLanguagename();
            music.setLanguagename(languageName);
        });
        return musicDTOList;
    }
}