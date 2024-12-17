package com.qqmusic.controller;

import com.qqmusic.dto.Result;
import com.qqmusic.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    CommentService commentServicel;
    @GetMapping("/list")
    public Result commentList(){
        return commentServicel.commentList();
    }
    @GetMapping("/user/{account}")
    public Result userCommentList(@PathVariable("account") String account)
    {
        return commentServicel.userCommentList(account);
    }
    @GetMapping("/music/searchByMusicId/{musicId}")
    public Result musicCommentList(@PathVariable("musicId") String musicId)
    {
        return commentServicel.searchByMusicId(musicId);
    }
    @GetMapping("/search")
    public  Result commentByContent(@RequestParam("content") String content)
    {
        return commentServicel.commentByContent(content);
    }
    @GetMapping("/music/searchByMusicName")
    public  Result searchByMusicName(@RequestParam("musicName") String musicName)
    {
        return commentServicel.searchByMusicName(musicName);
    }
    @PostMapping("/deleteByIds")
    public  Result deleteCommentById(@RequestBody String jsonData)
    {
        return commentServicel.deleteCommentByIds(jsonData);
    }
    @PostMapping("/insertComment")
    public  Result insertComment(@RequestBody String jsonData)
    {
        return commentServicel.insertComment(jsonData);
    }

}
