package com.xuexian.webkeshe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuexian.webkeshe.dto.getCourseDTO;
import com.xuexian.webkeshe.dto.CourseDTO;
import com.xuexian.webkeshe.entity.Course;
import com.xuexian.webkeshe.vo.Result;
import org.springframework.stereotype.Service;


public interface ICourseService extends IService<Course> {
    Result getCourseList(getCourseDTO courseDTO);



    Result selectCourse(Integer courseId);

    Result getSelectedCourse();

    Result deselectCourse(Integer courseId);

    Result addCourse(CourseDTO courseDTO);

    Result updateCourse(CourseDTO courseDTO);

    Result deleteCourse(Integer courseId);

    Result getCourse(Integer courseId);


}
