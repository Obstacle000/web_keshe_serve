package com.xuexian.webkeshe.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("class")
public class ClassInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 班级名称 */
    private String className;

    /** 班级编号（如 2021-CS-01） */
    private String classCode;

    /** 所属专业ID */
    private Integer specialtyId;
    @TableField(exist = false)
    private String specialtyName;

    private String grade;





    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;


}
