package com.qqmusic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqmusic.dto.MusicDTO;
import com.qqmusic.dto.Result;
import com.qqmusic.dto.SongListDTO;
import com.qqmusic.entity.Music;
import com.qqmusic.entity.Songlist;
import com.qqmusic.entity.SonglistMusic;
import com.qqmusic.entity.User;
import com.qqmusic.service.MusicService;
import com.qqmusic.service.SonglistMusicService;
import com.qqmusic.service.SonglistService;
import com.qqmusic.mapper.SonglistMapper;
import com.qqmusic.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

/**
 * @author gaoxiang
 * @description 针对表【songlist】的数据库操作Service实现
 * @createDate 2024-01-12 13:22:58
 */
@Service
public class SonglistServiceImpl extends ServiceImpl<SonglistMapper, Songlist>
        implements SonglistService{
    @Resource
    MusicService musicService;
    @Resource
    UserService userService;
    @Resource
    SonglistMusicService songlistMusicService;
    /**
     * 每个songlist在songlistMusic表中的数据查询一下对于的music表中是否存在,不存在就删除这条
     * @param list
     */
    public void sonlistMusicDeletIllegalMusic(List<Songlist> list)
    {
        LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SonglistMusic> songlistMusicWrapper = new LambdaQueryWrapper<>();

        for (Songlist songlist : list) {
            //新建一个songlistmusic要删除的数组,否则因为增强for会导致问题
            Set<Integer> todeletSonglistMusic = new HashSet<>();
            Set<Integer> todeleteSonglist = new HashSet<>();

            //查询这些歌单对应的user是否存在, 不存在就删除这些歌单
            User user = userService.searchByAccount(songlist.getUserAccount());
            if (user == null) todeleteSonglist.add(songlist.getSonglistId());

            songlistMusicWrapper.clear();
            Integer songlistId = songlist.getSonglistId();
            songlistMusicWrapper.eq(SonglistMusic::getSonglistId, songlistId);
            List<SonglistMusic> songListMusicList = songlistMusicService.list(songlistMusicWrapper);

            //用stream就不用再写一个for了
            List<String> musicIds = songListMusicList.stream()
                    .map(SonglistMusic::getMusicId)
                    .collect(Collectors.toList());
            //判断每首歌是否存在,不存在就将songlistMusic中的信息删除
            for (String musicId : musicIds) {
                musicWrapper.clear();
                musicWrapper.eq(Music::getMusicId, musicId);
                Music music = musicService.getOne(musicWrapper);
                //不存在,删除
                if (music == null) {
                    songlistMusicWrapper.clear();
                    songlistMusicWrapper.eq(SonglistMusic::getMusicId, musicId);
                    SonglistMusic one = songlistMusicService.getOne(songlistMusicWrapper);
                    todeletSonglistMusic.add(one.getId());
                }
            }
            //删除当前遍历歌单中无效的歌单
            removeByIds(todeleteSonglist);
            //删除当前遍历歌单中的无效音乐
            songlistMusicService.removeByIds(todeletSonglistMusic);
        }
    }

    /**
     * 展示该歌单的所有歌曲
     * @param songlistId
     * @return
     */
    @Override
    public Result showMusicsById(Integer songlistId) {
        //在songlist_music 中搜索歌单的id的所有行
        LambdaQueryWrapper<SonglistMusic> songlistMusicWrapper = new LambdaQueryWrapper<>();
        songlistMusicWrapper.eq(SonglistMusic::getSonglistId, songlistId);
        List<SonglistMusic> songlistMusicslist = songlistMusicService.list(songlistMusicWrapper);
        List<Music> musicList = BeanUtil.copyToList(songlistMusicslist, Music.class);
        List<MusicDTO> musicDTOs = new ArrayList<>();
        musicList.forEach(music -> {
            String musicId = music.getMusicId();
            //调用精准搜索歌曲的接口(自己写的)
            MusicDTO musicDTO = musicService.searchById(musicId);
            musicDTOs.add(musicDTO);
        });
        return Result.ok(musicDTOs);
    }

    /**
     * 给一个用户添加歌单
     * @param songlistname
     * @param account
     * @return
     */
    @Override
    public Result addSongList(String songlistname, String account) {
        //查询该用户是否有该歌单
        LambdaQueryWrapper<Songlist> songlistWrapper = new LambdaQueryWrapper<>();
        songlistWrapper.eq(Songlist::getSonglistname, songlistname).eq(Songlist::getUserAccount, account);
        Songlist songlistByName = getOne(songlistWrapper);
        //有
        if (songlistByName != null) return Result.fail("歌单"+ songlistname + "已存在!");
        //没有
        Songlist songlist = new Songlist();
        songlist.setSonglistname(songlistname);
        songlist.setUserAccount(account);
        DateTime dateTime = new DateTime();
        songlist.setCreateDate(dateTime.toString());
//        DateUtil.format(dateTime.,)
        save(songlist);
        List<Object> objects = new ArrayList<Object>();
        objects.add(songlist.getSonglistId());
        objects.add("添加歌单 " + songlistname + " 成功!");
        return Result.ok(objects);
    }

    /**
     * 删除一个用户的一个歌单
     * @param jsonData
     * @return
     */
    @Override
    public Result deleteSongLists(String jsonData) {
        JSONObject entries = JSONUtil.parseObj(jsonData);
        List<Integer> songlistIds = entries.getBeanList("songlistIds", Integer.class);
        LambdaQueryWrapper<Songlist> songlistWrapper = new LambdaQueryWrapper<>();
        songlistWrapper.in(Songlist::getSonglistId, songlistIds);
        boolean success = remove(songlistWrapper);
        if (success) return Result.ok("删除成功!");
        return Result.fail("删除失败!!");
    }

    /**
     *向用户的某一个歌单(批量)添加歌曲
     * @param jsonData
     * @return
     */
    @Override
    public Result addMusicToSongList(String jsonData) {
        JSONObject entries = JSONUtil.parseObj(jsonData);
        Integer account = entries.get("account", Integer.class);
        Integer songlistId = entries.get("songlistId", Integer.class);
        List<String> musicIds = entries.getBeanList("musicIds", String.class);

        //在songlist_music表中查询该歌单是否已经有该歌曲了
        LambdaQueryWrapper<SonglistMusic> songlistMusicWrapper = new LambdaQueryWrapper<>();
        for (String musicId : musicIds) {
            SonglistMusic songlistMusic = new SonglistMusic();
            songlistMusic.setSonglistId(songlistId);
            songlistMusicWrapper.clear();
            songlistMusicWrapper.eq(SonglistMusic::getSonglistId, songlistId);
            songlistMusicWrapper.eq(SonglistMusic::getMusicId, musicId);
            SonglistMusic getOne = songlistMusicService.getOne(songlistMusicWrapper);
            //有该歌曲就直接return
            if(getOne != null) continue;
            songlistMusic.setMusicId(musicId);
            songlistMusicService.save(songlistMusic);
        }
        return Result.ok("添加歌曲成功!");
    }

    /**
     * 展示一个用户的
     * @param account
     * @return
     */
    @Override
    public Result showUserSongListByAccount(String account) {
        //判断该用户是否存在
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<User>();
        userWrapper.eq(User::getAccount, account);
        User user = userService.getOne(userWrapper);
        if (user == null) return Result.fail("该用户不存在");
        //若该用户存在则继续查找该用户的歌单信息
        LambdaQueryWrapper<Songlist> songlistWrapper = new LambdaQueryWrapper<>();
        songlistWrapper.eq(Songlist::getUserAccount, account);
        List<Songlist> songList = list(songlistWrapper);
        return Result.ok(songList);
    }
    /**
     * 展示一个用户的所有歌单(ByName)
     */
    @Override
    public Result showUserSongListByName(String userName) {
        //判断该用户是否存在
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<User>();
        userWrapper.like(User::getUsername, userName);
        List<User> list = userService.list(userWrapper);
        if (list.isEmpty()) return Result.fail("该用户不存在");
        //若该用户存在则继续查找该用户的歌单信息
        List<User> userList = list;
        ArrayList<Songlist> songlists = new ArrayList<>();
        LambdaQueryWrapper<Songlist> songlistWrapper = new LambdaQueryWrapper<>();
        userList.stream().forEach(user->{
            String userAccount = user.getAccount();
            songlistWrapper.clear();
            songlistWrapper.eq(Songlist::getUserAccount, userAccount);
            List<Songlist> list1 = list(songlistWrapper);
            //设置每一个songlist的userName
            list1.forEach(songlist -> songlist.setUserName(user.getUsername()));
            songlists.addAll(list1);
            //删除歌单中不存在的歌曲和用户
            sonlistMusicDeletIllegalMusic(songlists);
        });
        return Result.ok(songlists);
    }

    @Override
    public Result getAll() {
        List<Songlist> list = list();
        //根据User表和Music表取出无效的歌单
        sonlistMusicDeletIllegalMusic(list);
        List<SongListDTO> songListDTOS = new ArrayList<>();
        //更新后记得重新获得一下,不然有一个被删了后会空指针1
        list = list();
        for (Songlist songlist : list) {
            //收集userName
            User user = userService.searchByAccount(songlist.getUserAccount());
            //收集musicName,
            List<MusicDTO> musicDTOS= (List<MusicDTO>) showMusicsById(songlist.getSonglistId()).getData();
            if (musicDTOS.isEmpty())
            {   SongListDTO songListDTO = new SongListDTO();
                BeanUtil.copyProperties(songlist, songListDTO);
                //设置userName
                songListDTO.setUserName(user.getUsername());
                //将新建的songlistDTO添加到集合中
                songListDTOS.add(songListDTO);
            }else{
                for (MusicDTO musicDTO : musicDTOS) {
                    SongListDTO songListDTO = new SongListDTO();
                    BeanUtil.copyProperties(songlist, songListDTO);
                    //设置userName
                    songListDTO.setUserName(user.getUsername());
                    //设置musicName
                    songListDTO.setMusicName(musicDTO.getMusicname());
                    //设置musicId
                    songListDTO.setMusicId(musicDTO.getMusicId());
                    //将新建的songlistDTO添加到集合中
                    songListDTOS.add(songListDTO);
                }
            }
        }
        return Result.ok(songListDTOS);
    }

    @Override
    public Result showSongListByName(String songlistName) {
        LambdaQueryWrapper<Songlist> songlistWrapper = new LambdaQueryWrapper<>();
        songlistWrapper.eq(Songlist::getSonglistname, songlistName);
        List<Songlist> list = list(songlistWrapper);
        sonlistMusicDeletIllegalMusic(list);
        list=list(songlistWrapper);
        return Result.ok(list);
    }

    @Override
    public Result bestMusicOfUser(String account, Integer isReverse) {
        LambdaQueryWrapper<Songlist> songlistWrapper = new LambdaQueryWrapper<>();
        songlistWrapper.eq(Songlist::getUserAccount, account);
        List<Songlist> list = list(songlistWrapper);
        sonlistMusicDeletIllegalMusic(list);
        list=list(songlistWrapper);
        List<MusicDTO> musicDTOS = bestMusicsByList(list, isReverse);
        return Result.ok(musicDTOS);
    }

    @Override
    public Result bestMusics(Integer isReverse) {
        List<Songlist> songlists = list();
        sonlistMusicDeletIllegalMusic(songlists);
        songlists=list();
        List<MusicDTO> musicDTOS = bestMusicsByList(songlists, isReverse);
        return Result.ok(musicDTOS);
    }

    @Override
    public Result updateSonglist(Songlist songlist) {
        saveOrUpdate(songlist);
        return Result.ok("更新成功");
    }


    public List<MusicDTO> bestMusicsByList(List<Songlist> songlists, Integer isReverse) {
        //获取songlistId
        List<Integer> songlistIds = songlists.stream().map(Songlist::getSonglistId).collect(Collectors.toList());
        LambdaQueryWrapper<SonglistMusic> songlistMusicWrapper = new LambdaQueryWrapper<>();
       //查询这个歌单的所有歌曲
        songlistMusicWrapper.in(SonglistMusic::getSonglistId, songlistIds);
        List<SonglistMusic> songlistMusics = songlistMusicService.list(songlistMusicWrapper);
        Map<String, Integer> musicIdCountMap = new HashMap<>();
        // 统计相同 musicId 的数量
        for (SonglistMusic songlistMusic : songlistMusics) {
            String musicId = songlistMusic.getMusicId();
            Integer count = musicIdCountMap.getOrDefault(musicId, 0);
            musicIdCountMap.put(musicId, count + 1);
        }
        //查询每个music的详细信息并设置对应的beLovedCounts
        List<MusicDTO> musicDTOS = musicIdCountMap.entrySet().stream().map(entry -> {
            Music music = musicService.searchByMusicId(entry.getKey());
            ArrayList<Music> musics = new ArrayList<>();
            musics.add(music);
            List<MusicDTO> temp = musicService.musicToDTo(musics);
            MusicDTO musicDTO = temp.get(0);
            musicDTO.setBeLovedCounts(entry.getValue());
            return musicDTO;
        }).collect(Collectors.toList());

        List<MusicDTO> sortedMusicDTOS = null;
        if (isReverse.equals(1)) {
            sortedMusicDTOS = musicDTOS.stream()
                    .sorted(Comparator.comparingInt(MusicDTO::getBeLovedCounts).reversed())
                    .collect(Collectors.toList());
        }else{
            sortedMusicDTOS = musicDTOS.stream()
                    .sorted(Comparator.comparingInt(MusicDTO::getBeLovedCounts))
                    .collect(Collectors.toList());
        }
        return sortedMusicDTOS;
    }


//    @Override
//    public Result showMusicsByName(String songListName) {
//        //先查询是否有这个歌单
//        LambdaQueryWrapper<Songlist> songlistWrapper = new LambdaQueryWrapper<>();
//        songlistWrapper.eq(Songlist::getSonglistname, songListName);
//        this.getList
//        //在songlist_music 中搜索歌单的id的所有行
//        LambdaQueryWrapper<SonglistMusic> songlistMusicWrapper = new LambdaQueryWrapper<>();
//        songlistMusicWrapper.eq(SonglistMusic::getSonglistId, songlistId);
//        List<SonglistMusic> songlistMusicslist = songlistMusicService.list(songlistMusicWrapper);
//        List<Music> musicList = BeanUtil.copyToList(songlistMusicslist, Music.class);
//        List<MusicDTO> musicDTOs = new ArrayList<>();
//        musicList.forEach(music -> {
//            String musicId = music.getMusicId();
//            //调用精准搜索歌曲的接口(自己写的)
//            MusicDTO musicDTO = musicService.searchById(musicId);
//            musicDTOs.add(musicDTO);
//        });
//        return Result.ok(musicDTOs);
//    }
}




