package com.xuexian.webkeshe.controller.student;


import com.xuexian.webkeshe.dto.IdsDTO;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.dto.UserDTO;
import com.xuexian.webkeshe.entity.Student;
import com.xuexian.webkeshe.service.IStudentService;

import com.xuexian.webkeshe.util.UserHolder;
import com.xuexian.webkeshe.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.xuexian.webkeshe.util.Code.REQUEST_SUCCESS;


/**
 * StudentController 主要是为学生管理数据管理提供的Web请求服务
 */

// origins： 允许可访问的域列表
// maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。
@RestController
@RequestMapping("/api")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    /**
     * 获取所有学生表格信息
     * @param pageDTO
     * @return
     */
    @PostMapping("/getStuInfo")
    public Result getStuInfo(@RequestBody PageDTO pageDTO) {
        return Result.success(REQUEST_SUCCESS,studentService.getStudentList(pageDTO));
    }

    /**
     * 仅添加学生
     * @param student
     * @return
     */
    @PostMapping("/addStu")
    public Result addStu(@RequestBody Student student) {
        UserDTO user = UserHolder.getUser();
        if(user.getRole()!= 2||user.getRole()!= 3){
            return Result.error("权限不足");
        }

        return studentService.addStu(student);
    }

    @PostMapping("/delStu")
    public Result deleteStudents(@RequestBody IdsDTO ids) {
        UserDTO user = UserHolder.getUser();
        if(user.getRole()!= 2||user.getRole()!= 3){
            return Result.error("权限不足");
        }
        studentService.removeBatchByIds(ids.getIds());
        return Result.success(REQUEST_SUCCESS,null);
    }

    @PostMapping("/updateStu")
    public Result updateStu(@RequestBody Student student) {
        UserDTO user = UserHolder.getUser();
        if(user.getRole()!= 2||user.getRole()!= 3){
            return Result.error("权限不足");
        }

        return studentService.updateStu(student);
    }




}
