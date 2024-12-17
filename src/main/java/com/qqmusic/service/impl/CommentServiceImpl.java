package com.qqmusic.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqmusic.dto.Result;
import com.qqmusic.entity.Comment;
import com.qqmusic.entity.Music;
import com.qqmusic.entity.User;
import com.qqmusic.service.CommentService;
import com.qqmusic.mapper.CommentMapper;
import com.qqmusic.service.MusicService;
import com.qqmusic.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoxiang
 * @description 针对表【tb_comment】的数据库操作Service实现
 * @createDate 2024-01-12 13:22:58
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService{

    @Resource
    MusicService musicService;
    @Resource
    UserService userService;
    private void addUserName(List<Comment> list)
    {
        list.forEach(comment->{
            String userAccount = comment.getUserAccount();
            User user = userService.searchByAccount(userAccount);
            comment.setUserName(user.getUsername());
        });
    }

    @Override
    public Result commentByContent(String content) {
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.like(Comment::getContent, content);
        List<Comment> list = list(commentWrapper);
        deletIllegalCommentByMusic(list);
        deletIllegalCommentByUser(list);
        addUserName(list);
        return Result.ok(list);
    }

    @Override
    public Result deleteCommentByIds(String jsonData) {
        JSONObject entries = JSONUtil.parseObj(jsonData);
        List<Integer> commentIds = entries.getBeanList("comment_ids", Integer.class);
        System.out.println(commentIds);
        boolean success = removeBatchByIds(commentIds);
        if (success) return Result.ok("删除成功!");
        return Result.fail("抱歉!删除失败");
    }

    @Override
    public Result searchByMusicName(String musicName) {
        LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
        musicWrapper.eq(Music::getMusicname, musicName);
        Music music = musicService.getOne(musicWrapper);
        String musicId = music.getMusicId();
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getMusicId, musicId);
        List<Comment> list = list(commentWrapper);
        deletIllegalCommentByUser(list);
        deletIllegalCommentByMusic(list);
        addUserName(list);
        return Result.ok(list);
    }

    @Override
    public Result insertComment(String jsonData) {
        JSONObject entries = JSONUtil.parseObj(jsonData);
        Comment commentToInstert = JSONUtil.toBean(entries, Comment.class);
        DateTime date = DateUtil.date();
        commentToInstert.setCommentTime(date.now().toString());
        save(commentToInstert);
        ArrayList<Comment> list = new ArrayList<>();
        list.add(commentToInstert);
        deletIllegalCommentByMusic(list);
        deletIllegalCommentByUser(list);
        return Result.ok("插入成功!");
    }



    @Override
    public Result commentList() {
        List<Comment> list = list();
        //展示之前查看一下该这条评论的musicId是否存在
        deletIllegalCommentByMusic(list);
        for (Comment comment : list) {
            Music music = musicService.searchByMusicId(comment.getMusicId());
            comment.setMusicName(music.getMusicname());
        }
        addUserName(list);
        return Result.ok(list);
    }

    public void deletIllegalCommentByMusic(List<Comment> list)
    {
        LambdaQueryWrapper<Music> musicWrapper = new LambdaQueryWrapper<>();
        List<Comment> temps = new ArrayList<>();
        for (Comment comment : list) {
            //将不存在的音乐的评论删掉
            musicWrapper.clear();
            String musicId = comment.getMusicId();
            musicWrapper.eq(Music::getMusicId, musicId);
            Music music = musicService.getOne(musicWrapper);
            if (music == null) {
                Integer commentId = comment.getCommentId();
                removeById(commentId);
                temps.add(comment);
            }
        }
        list.removeAll(temps);//在if里面直接remove会因为list增强for的一些原理报错
    }
    public void deletIllegalCommentByUser(List<Comment> list)
    {
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        List<Comment> temps = new ArrayList<>();
        for (Comment comment : list) {
            //将不存在的用户的评论删掉
            userWrapper.clear();
            String userAccount = comment.getUserAccount();
            userWrapper.eq(User::getAccount, userAccount);
            User user = userService.getOne(userWrapper);
            if (user == null) {
                Integer commentId = comment.getCommentId();
                removeById(commentId);
                temps.add(comment);
            }
        }
        list.removeAll(temps);//在if里面直接remove会因为list增强for的一些原理报错
    }
    @Override
    public Result userCommentList(String account)
    {
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getUserAccount, account);
        List<Comment> list = list(commentWrapper);
        deletIllegalCommentByUser(list);
        deletIllegalCommentByMusic(list);
        addUserName(list);
        return Result.ok(list);
    }

    @Override
    public Result searchByMusicId(String musicId)
    {
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getMusicId, musicId);
        List<Comment> list = list(commentWrapper);
        deletIllegalCommentByUser(list);
        deletIllegalCommentByMusic(list);
        addUserName(list);
        return Result.ok(list);
    }

}




