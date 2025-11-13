package com.xuexian.webkeshe.controller.role;

import com.xuexian.webkeshe.service.IRoleService;
import com.xuexian.webkeshe.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.xuexian.webkeshe.util.Code.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api")
@Slf4j
public class RoleController {

    @Autowired
    private IRoleService roleService; // ✅ 放在类内部

    @PostMapping("/getRoleList")
    public Result getRoleList() {
        List<String> roleList = roleService.getRoleList();
        return Result.success(REQUEST_SUCCESS, roleList);
    }
}
