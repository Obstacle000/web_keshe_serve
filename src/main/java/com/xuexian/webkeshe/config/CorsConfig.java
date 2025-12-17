package com.xuexian.webkeshe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    // 超级大坑,用这个最好不要用@Cross这个注解,因为如果allowedOrigins改成(*)的话,可能会导致option预检ok,但是post发不出去的问题
    // 就是Spring Boot CORS 配置冲突,幸好用postman了,有报错信息
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")              // 匹配所有接口
                .allowedOrigins("http://localhost:3000") // 允许前端地址访问
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许请求方法
                .allowCredentials(true)         // 允许携带 cookie
                .allowedHeaders("*");           // 允许所有请求头
    }
}

