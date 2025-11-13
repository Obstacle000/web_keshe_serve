package com.xuexian.webkeshe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuexian.webkeshe.entity.Role;

import java.util.List;

public interface IRoleService extends IService<Role> {
    List<String> getRoleList();
}
