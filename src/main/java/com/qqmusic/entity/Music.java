package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_music
 */
@TableName(value ="tb_music")
@Data
public class Music implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String musicname;

    /**
     * 
     */
    private String musicUrl;

    /**
     * 
     */
    private Integer styleId;

    /**
     * 
     */
    private Integer languageId;

    /**
     * 
     */
    private Integer singerId;

    /**
     * 
     */
    private String lyricUrl;

    /**
     * 
     */
    private String coverUrl;

    /**
     * 
     */
    private Integer downloadCount;

    /**
     * 
     */
    private Integer likeCount;

    /**
     * 
     */
    private Integer commentCount;

    /**
     * 
     */
    private String introduction;

    /**
     * 
     */
    private String musicId;

    /**
     * 
     */
    private String releaseDate;

    /**
     * 
     */
    private Integer duration;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}