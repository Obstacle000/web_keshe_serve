package com.xuexian.webkeshe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("college")
public class College implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 学院名称 */
    private String collegeName;

    /** 学院代码（可选，唯一标识） */
    private String collegeCode;



    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
