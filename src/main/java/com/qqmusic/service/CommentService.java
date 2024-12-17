package com.qqmusic.service;

import com.qqmusic.dto.Result;
import com.qqmusic.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author gaoxiang
* @description 针对表【tb_comment】的数据库操作Service
* @createDate 2024-01-16 10:04:22
*/
public interface CommentService extends IService<Comment> {

    Result commentList();

    Result userCommentList(String account);

    Result searchByMusicId(String musicId);

    Result commentByContent(String content);

    Result deleteCommentByIds(String jsonData);

    Result searchByMusicName(String musicName);

    Result insertComment(String jsonData);

}
