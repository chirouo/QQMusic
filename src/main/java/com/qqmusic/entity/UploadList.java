package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName upload_list
 */
@TableName(value ="upload_list")
@Data
public class UploadList implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer uploadId;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer musicId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}