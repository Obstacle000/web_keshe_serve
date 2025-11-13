package com.xuexian.webkeshe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("student")
public class Student implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 学号，唯一 */
    private String stuId;

    /** 学生姓名 */
    private String name;

    /** 性别：0男 1女 */
    private Integer sex;

    /** 出生日期 */
    private LocalDate birthday;

    /** 年龄 */
    private Integer age;

    /** 生源地/家庭住址 */
    private String address;

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

    /** 所属班级ID */
    private Integer classId;
    @TableField(exist = false)
    private String className;

    private Integer userId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
