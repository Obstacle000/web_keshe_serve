package com.xuexian.webkeshe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Course {

    /** 课程ID */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 课程名称 */
    private String courseName;

    /** 课程编号 */
    private String courseCode;

    /** 学分 */
    private BigDecimal credit;

    /** 任课教师ID */
    private Integer teacherId;
    @TableField(exist = false)
    private String teacherName;


    /** 所属学院ID */
    private Integer collegeId;
    @TableField(exist = false)
    private String collegeName;

    /** 课程状态：1正常 0停开 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 课程时间(第几节) */
    private Integer time;




}
