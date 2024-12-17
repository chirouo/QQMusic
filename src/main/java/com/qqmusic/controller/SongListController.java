package com.qqmusic.controller;

import cn.hutool.json.JSONUtil;
import com.qqmusic.dto.Result;

import com.qqmusic.entity.Songlist;
import com.qqmusic.service.SonglistService;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/songlist")
public class SongListController {
   @Resource
    SonglistService songlistService;
    @GetMapping("/addSongList")//wtj非要我设计成get
    public Result addSongList(@RequestParam("songlistname") String songlistname, @RequestParam("account") String account)
    {
        return songlistService.addSongList(songlistname, account);
    }
    @PostMapping("/deleteSongLists")
    public Result deleteSongLists(@RequestBody String jsonData)
    {
        return songlistService.deleteSongLists(jsonData);
    }
    @PostMapping("/addMusics")
    public Result addMusicToSongList(@RequestBody String jsonData)
    {
        return songlistService.addMusicToSongList(jsonData);
    }
    @GetMapping("showMusicsById/{songListId}")
    public Result showMusicsById(@PathVariable("songListId") Integer songListId)
    {
        return songlistService.showMusicsById(songListId);
    }

//    @GetMapping("/showMusicsByName")
//    public Result showMusicsByName(@RequestParam String songListName)
//    {
//        return songlistService.showMusicsByName(songListName);
//    }
    @GetMapping("/{account}")
    public Result showUserSonglistByAccount(@PathVariable("account") String account)
    {
        return songlistService.showUserSongListByAccount(account);
    }
    @GetMapping("")
    public Result showUserSonglistByName(@RequestParam("userName") String userName)
    {
        return songlistService.showUserSongListByName(userName);
    }
    @GetMapping("/getAll")
    public Result getAll() {
        return songlistService.getAll();
    }
    @GetMapping("/showSonglistByName")
    public Result showSonglistByName(@RequestParam("songlistName") String songlistName)
    {
        return songlistService.showSongListByName(songlistName);
    }
    @GetMapping("/bestMusics/{isReverse}")
    public Result bestMusics (@PathVariable("isReverse") Integer isReverse)
    {
        return songlistService.bestMusics(isReverse);
    }
    @GetMapping("/betsMusicsOfUser/{account}/{isReverse}")
    public Result bestMusicsOfUser(@PathVariable("account") String account, @PathVariable("isReverse") Integer isReverse)
    {
        return songlistService.bestMusicOfUser(account, isReverse);
    }
    @PostMapping("/updateSonglist")
    public Result updateSonglist(@RequestBody Songlist songlist)
    {
        return songlistService.updateSonglist(songlist);
    }
}
