package com.qqmusic.dto;
import lombok.Data;

@Data
public class SongListDTO {

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
    private String coverUrl;

    /**
     *
     */
    private String createDate;
    private String musicId;
    private String userName;

    private String musicName;
}
