package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName songlist_music
 */
@TableName(value ="songlist_music")
@Data
public class SonglistMusic implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer songlistId;

    /**
     * 
     */
    private String musicId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}