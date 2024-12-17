package com.qqmusic.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qqmusic.dto.MusicDTO;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.Music;
import com.qqmusic.entity.Songlist;
import com.qqmusic.service.MusicService;
import com.qqmusic.service.SonglistService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {
    @Resource
    private MusicService musicService;@GetMapping("/list")
    public Result getMusicList(){
        return musicService.getMusicList();
    }
    @GetMapping()
    public Result searchMusics(@RequestParam("musicname") String musicname){
        return  musicService.searchMuscic(musicname);
    }
    @PostMapping()
    public Result delMusics(@RequestBody String jsonData)
    {
//        System.out.println(jsonData);
        JSONObject data = JSONUtil.parseObj(jsonData);
        JSONArray musicIds = data.getJSONArray("musicIds");
        List<String> list = JSONUtil.toList(musicIds, String.class);
        return musicService.delMusics(list);
//        return null;
    }
    @PostMapping("/insertMusic")
    public Result insertMusic (@RequestBody String jsonData)
    {
//        System.out.println(jsonData);
        JSONObject data = JSONUtil.parseObj(jsonData);
        JSONArray musicDTOs = data.getJSONArray("musics");
        List<MusicDTO> musicDTOlist = JSONUtil.toList(musicDTOs, MusicDTO.class);
        return musicService.insertMusics(musicDTOlist);
//        return null;
    }
    @GetMapping("searchById/{musicId}")
    public Result searchById(@PathVariable("musicId") String musicId)
    {
        ArrayList<MusicDTO> musicDTOS = new ArrayList<>();
        musicDTOS.add(musicService.searchById(musicId));
        return Result.ok(musicDTOS);
    }
    @PutMapping("updateByIds")
    public  Result updateByIds(@RequestBody String jsonData)
    {
        return musicService.updateByIds(jsonData);
//        System.out.println("------------这是打印");
//        return null;
    }
    @GetMapping("searchByStyle")
    public Result searchByStyle(@RequestParam("style") String styleName)
    {
        return musicService.searchByStyle(styleName);
    }
    @GetMapping("searchBySinger")
    public Result searchBySinger(@RequestParam("singer") String singerName)
    {
        return musicService.searchBySinger(singerName);
    }
}
