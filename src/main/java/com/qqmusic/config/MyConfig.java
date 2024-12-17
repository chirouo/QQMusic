//package com.qqmusic.config;
//
//import com.qqmusic.utils.LoginInterceptor;
//import com.qqmusic.utils.RefreshTokenInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.Resource;
//
//@Configuration
//public class MyConfig implements WebMvcConfigurer {
//    @Resource
//    private StringRedisTemplate stringRedisTemplate;//该类中有一个@Configuration注解说明该类可以由spring来帮我们管理对象
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {//再该类中让spring帮我们管理对象然后传给LoginInterceptor
//        registry.addInterceptor(new LoginInterceptor()).excludePathPatterns(// 匿名对象 stream coding
//                "/user/code",
//                "/voucher/**",
//                "/blog/hot",
//                "/shop/**",
//                "/shop-type/**",
//                "/upload/**",
//                "/user/login"
//        ).order(1);
//        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);
//    }
//}
