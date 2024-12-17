package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_language
 */
@TableName(value ="tb_language")
@Data
public class Language implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer languageId;

    /**
     * 
     */
    private String languagename;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}