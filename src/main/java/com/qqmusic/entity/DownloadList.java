package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName download_list
 */
@TableName(value ="download_list")
@Data
public class DownloadList implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer downloadId;

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