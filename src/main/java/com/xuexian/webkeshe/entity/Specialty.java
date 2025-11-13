package com.xuexian.webkeshe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("specialty")
public class Specialty implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 专业名称 */
    private String specialtyName;

    /** 专业代码（唯一标识） */
    private String specialtyCode;

    /** 所属学院ID */
    private Integer collegeId;


    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
