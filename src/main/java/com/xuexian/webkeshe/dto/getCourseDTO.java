package com.xuexian.webkeshe.dto;

import lombok.Data;

@Data
public class getCourseDTO {
    private Integer  collegeId;
    private PageDTO page;
    private String name;
}
