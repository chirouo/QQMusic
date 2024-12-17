package com.qqmusic.controller;

import com.qqmusic.dto.Result;
import com.qqmusic.service.StyleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/style")
public class StyleController {
    @Resource
    StyleService styleService;
    @GetMapping("/list")
    public Result showstyleList()
    {
        return styleService.showStyleList();
    }
}
