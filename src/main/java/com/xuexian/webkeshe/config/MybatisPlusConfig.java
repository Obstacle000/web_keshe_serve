package com.xuexian.webkeshe.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor myInterceptor = new MybatisPlusInterceptor();

        myInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType
                .MYSQL));

        // 添加其他拦截器

        return myInterceptor;
    }
}
