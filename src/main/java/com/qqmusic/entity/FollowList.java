package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

/**
 * 
 * @TableName follow_list
 */
@TableName(value ="follow_list")
@Data
@Accessors(chain = true)
public class FollowList implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer followId;

    /**
     * 
     */
    private String followerId;

    /**
     * 
     */
    private String befollowedId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}