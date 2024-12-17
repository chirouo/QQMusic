package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_singer
 */
@TableName(value ="tb_singer")
@Data
public class Singer implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer singerId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}