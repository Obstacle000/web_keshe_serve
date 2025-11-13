package com.xuexian.webkeshe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuexian.webkeshe.entity.Role;
import com.xuexian.webkeshe.mapper.RoleMapper;
import com.xuexian.webkeshe.service.IRoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<String> getRoleList() {
        List<Role> allRoles = roleMapper.getAllRoles();
        List<String> roleNames = allRoles.stream()
                .map(Role::getRoleName)  // 提取每个 role 的 roleName
                .collect(Collectors.toList());

        return roleNames;
    }
}
