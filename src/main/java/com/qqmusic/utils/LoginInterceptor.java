package com.qqmusic.utils;

import com.qqmusic.dto.UserDTO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.从UserHolder获得用户
        UserDTO userDto = UserHolder.getUser();
        //2.判断是否有user用户
        if (userDto == null) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
