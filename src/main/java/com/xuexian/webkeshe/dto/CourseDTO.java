package com.xuexian.webkeshe.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data

public class CourseDTO {
    private Integer id;
    private String courseName;
    private String courseCode;
    private BigDecimal credit;
    private Integer teacherId;
    private Integer collegeId;
    private Integer status;
    private Integer time;
}
