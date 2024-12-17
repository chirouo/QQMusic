package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_comment
 */
@TableName(value ="tb_comment")
@Data
public class Comment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer commentId;

    /**
     * 
     */
    private String musicId;

    /**
     * 
     */
    private String userAccount;

    /**
     * 
     */
    private String content;

    /**
     * 
     */

    private String commentTime;
    @TableField(exist = false)
    private String musicName;
    @TableField(exist = false)
    private String userName;

    private static final long serialVersionUID = 1L;
}