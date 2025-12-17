package com.xuexian.webkeshe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String nickName;
    private Integer role;




    /** 安全获取角色列表，避免 roles 为 null */
    public Integer getRoles() {
        return role;
    }
}
