package com.xuexian.webkeshe.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("teacher")
public class Teacher implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 教师工号，唯一 */
    private String teacherId;

    /** 教师姓名 */
    private String name;

    private Integer userId;

    /** 性别：0男 1女 */
    private Integer sex;

    /** 出生日期 */
    private LocalDate birthday;

    /** 年龄 */
    private Integer age;

    /** 联系方式 */
    private String phoneNo;

    /** 所属学院ID */
    private Integer collegeId;
    @TableField(exist = false)
    private String collegeName;

    /** 所属专业ID */
    private Integer specialtyId;
    @TableField(exist = false)
    private String specialtyName;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}

