package com.qqmusic.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.qqmusic.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.qqmusic.utils.RedisConstants.LOGIN_USER_KEY;
import static com.qqmusic.utils.RedisConstants.LOGIN_USER_TTL;

public class RefreshTokenInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;//用来接住外部传过来的StringRedisTemplate
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获得token, 判断token是否为空
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            return true;//均return true, 判断是否有用户的任务交给下一层,本层的任务是存储user到当前thread+刷新token
        }
        //2.通过token获取redis中的用户信息,并判断同户是否存在,不存在返回false,并设置状态码
        String tokenKey = LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(tokenKey);
        if (userMap.isEmpty()){//及时为空也会被包装成一个对象,因此这里不能判断null而是isEmpty
            return true;
        }
        //3.将map变成userDto
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        //4.存在就将当前用户存储在ThreadLocal中
  UserHolder.saveUser(userDTO);
        //5.每拦截一次, redis中存储的用户信息刷新一次
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户,防止内存泄漏
        UserHolder.removeUser();
    }
}
