package com.xuexian.webkeshe.config;


import com.xuexian.webkeshe.interceptor.JwtInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login", "/doc.html","/auth/casLogin","/error",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/favicon.ico",
                        "/api/getRoleList",
                        "/api/getAllCollege",
                        "/api/getSpecialtyByCollege",
                        "/api/getClassBySpecialty",
                        "/api/getTeacherList");
    }

}
