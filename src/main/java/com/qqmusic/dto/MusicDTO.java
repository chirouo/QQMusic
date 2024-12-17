package com.qqmusic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class MusicDTO {
    private String style;
    private String languagename;
    private String singername;


    private String musicId;

    /**
     *
     */
    private String musicname;

    /**
     *
     */
    private String musicUrl;

    /**
     *
     */
    private Integer styleId;

    /**
     *
     */
    private Integer languageId;

    /**
     *
     */
    private Integer singerId;

    /**
     *
     */
    private String releaseDate;

    /**
     *
     */
    private String lyricUrl;

    /**
     *
     */
    private String coverUrl;

    /**
     *
     */
    private Integer downloadCount;

    /**
     *
     */
    private Integer likeCount;

    /**
     *
     */
    private Integer commentCount;

    /**
     *
     */
    private String introduction;
    private Integer duration;
    @JsonInclude(NON_NULL)
    private Integer beLovedCounts;
    private MultipartFile lyricFile;
    private MultipartFile coverFile;
    private MultipartFile musicFile;
}
