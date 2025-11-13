package com.xuexian.webkeshe.dto;

import lombok.Data;

/**
 * 用于接收前端管理员新增/修改请求参数的 DTO
 */
@Data
public class AdminDTO {

    private Integer id;
    /** 要添加的管理员账号名称 */
    private String name;

    /** 要添加的管理员密码 */
    private String password;

    /** 要添加的管理员角色ID */
    private Integer role;

    /** 备注信息 */
    private String remark;

    /** 当前操作人用户名（执行添加操作的人） */
    private String user;

    /** 当前操作人的角色（例如 1 表示超级管理员） */
    private Integer currentRole;
}

