package com.xuexian.webkeshe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("role")
@Data
public class Role {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private String roleName;


    private String description;

    private LocalDateTime createTime;


    private LocalDateTime updateTime;

}
