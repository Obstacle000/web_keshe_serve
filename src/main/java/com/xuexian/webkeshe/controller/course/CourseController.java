package com.xuexian.webkeshe.controller.course;

import com.xuexian.webkeshe.dto.CourseDTO;
import com.xuexian.webkeshe.dto.getCourseDTO;
import com.xuexian.webkeshe.entity.Course;
import com.xuexian.webkeshe.service.ICourseService;
import com.xuexian.webkeshe.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class CourseController {
    @Autowired
    private ICourseService courseService;
    @PostMapping("/getCourseList")
    public Result getCourseList(@RequestBody getCourseDTO courseDTO){
        return courseService.getCourseList(courseDTO);

    }

    @PostMapping("/selectCourse")
    public Result selectCourse(Integer courseId){
        return courseService.selectCourse(courseId);

    }
    @PostMapping("/deselectCourse")
    public Result deselectCourse(Integer courseId){
        return courseService.deselectCourse(courseId);

    }

    @PostMapping("/getSelectedCourse")
    public Result getSelectedCourse(){
        return courseService.getSelectedCourse();
    }

    @PostMapping("/addCourse")
    public Result addCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO);
    }

    @PostMapping("/updateCourse")
    public Result updateCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.updateCourse(courseDTO);
    }

    @PostMapping("/deleteCourse")
    public Result deleteCourse( Integer courseId) {
        return courseService.deleteCourse(courseId);
    }

    @PostMapping("/getCourse")
    public Result getCourse(Integer courseId) {
        return courseService.getCourse(courseId);
    }


}
