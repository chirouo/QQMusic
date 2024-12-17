package com.qqmusic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @TableName tb_user
 */
@TableName(value ="tb_user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String account;

    /**
     * 
     */
    private String gender;

    /**
     * 
     */
    private Integer age;

    /**
     * 
     */
    private String avatarUrl;

    /**
     * 
     */
    private String signature;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private Integer level;

    /**
     * 
     */
    private String role;
    @TableField(exist = false)
    private MultipartFile imageFile;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}