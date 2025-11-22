package com.xuexian.webkeshe.controller.teacher;


import com.xuexian.webkeshe.dto.IdsDTO;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.entity.Student;
import com.xuexian.webkeshe.entity.Teacher;
import com.xuexian.webkeshe.service.ITeacherService;
import com.xuexian.webkeshe.vo.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.xuexian.webkeshe.util.Code.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api")
public class TeacherController {
    @Autowired
    private ITeacherService teacherService;
    @PostMapping("/getTeacherInfo")
    public Result getTeacherInfo(@RequestBody PageDTO pageDTO) {
        return Result.success(REQUEST_SUCCESS,teacherService.getTeacherList(pageDTO));
    }


    @PostMapping("/addTeacher")
    public Result addTeacher(@RequestBody Teacher teacher) {

        return teacherService.addTeacher(teacher);
    }

    @PostMapping("/delTeacher")
    public Result deleteStudents(@RequestBody IdsDTO ids) {
        teacherService.removeBatchByIds(ids.getIds());
        return Result.success(REQUEST_SUCCESS,null);
    }

    @PostMapping("/updateTeacher")
    public Result updateStu(@RequestBody Teacher Teacher) {

        return teacherService.updateTeacher(Teacher);
    }



}
