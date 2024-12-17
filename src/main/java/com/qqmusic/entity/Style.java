package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_style
 */
@TableName(value ="tb_style")
@Data
public class Style implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer styleId;

    /**
     * 
     */
    private String style;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}