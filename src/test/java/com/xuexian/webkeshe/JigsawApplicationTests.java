package com.xuexian.webkeshe;

import com.xuexian.webkeshe.util.PasswordEncoder;

import org.junit.jupiter.api.Test;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebkesheApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.matches("123456", "tgy6b2yfuxbsbk6lbf4k@bda56fdf3aa5ee80c45612e6f47e439a"));
    }




}
