package com.xuexian.webkeshe.controller.admin;

import com.xuexian.webkeshe.dto.AdminDTO;
import com.xuexian.webkeshe.service.IUserService;
import com.xuexian.webkeshe.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class AdminController {
    @Autowired
    private IUserService userService;

    @PostMapping("/addAdmin")
    public Result addAdmin(@RequestBody AdminDTO admin) {


        return userService.addAdmin(admin);

    }

}
