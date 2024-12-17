package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName songlist
 */
@TableName(value ="songlist")
@Data
public class Songlist implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer songlistId;

    /**
     * 
     */
    private String songlistname;

    /**
     * 
     */
    private String userAccount;

    /**
     * 
     */
    @TableField(exist = false)
    private String coverUrl;

    /**
     * 
     */
    private String createDate;
    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}