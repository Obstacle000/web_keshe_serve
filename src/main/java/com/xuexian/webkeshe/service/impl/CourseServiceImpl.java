package com.xuexian.webkeshe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuexian.webkeshe.dto.getCourseDTO;
import com.xuexian.webkeshe.entity.*;

import com.xuexian.webkeshe.mapper.CollegeMapper;
import com.xuexian.webkeshe.mapper.CourseMapper;
import com.xuexian.webkeshe.mapper.StudentCourseMapper;
import com.xuexian.webkeshe.mapper.StudentMapper;
import com.xuexian.webkeshe.service.*;
import com.xuexian.webkeshe.util.Code;
import com.xuexian.webkeshe.util.UserHolder;
import com.xuexian.webkeshe.vo.PageVO;
import com.xuexian.webkeshe.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.xuexian.webkeshe.util.Code.REQUEST_SUCCESS;
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IUserService userService;

    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private CollegeMapper collegeMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Result getCourseList(getCourseDTO courseDTO) {
        // 获取分页参数
        int pageNo = courseDTO.getPage() != null ? courseDTO.getPage().getPageNo() : 1;
        int pageSize = courseDTO.getPage() != null ? courseDTO.getPage().getPageSize() : 10;

        // 获取筛选条件
        Integer collegeId = courseDTO.getCollegeId();
        String name = courseDTO.getName();

        // 构造查询条件
        QueryWrapper<Course> query = new QueryWrapper<>();
        if (collegeId != null) {
            query.eq("college_id", collegeId);
        }
        if (name != null && !name.isEmpty()) {
            query.like("course_name", name);
        }
        query.eq("status", 1);  // 只查询正常课程

        // 分页查询
        Page<Course> page = new Page<>(pageNo, pageSize);
        Page<Course> resultPage = page(page, query);
        resultPage.getRecords().stream().forEach(course -> {
            Teacher t = teacherService.query().eq("id", course.getTeacherId()).one();
            College college = collegeMapper.selectById(course.getCollegeId());
            course.setCollegeName(college.getCollegeName());
            course.setTeacherName(t.getName());
        });
        PageVO<Course> p = new PageVO<>(resultPage.getTotal(), resultPage.getRecords());


        return Result.success(REQUEST_SUCCESS,p);
    }

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @Override
    public Result selectCourse(Integer courseId) {
        User byId = userService.getById(UserHolder.getUser().getId());
        String studentId = byId.getUserName();
        Student studentByStuId = studentMapper.getStudentByStuId(studentId);
        if (studentId == null) {
            return Result.error("未登录");
        }

        // 判断是否已选
        QueryWrapper<CourseStudent> query = new QueryWrapper<>();
        query.eq("student_id", studentId).eq("course_id", courseId);
        CourseStudent exist = studentCourseMapper.selectOne(query);
        if (exist != null) {
            return Result.error(Code.COURSE_SELECTED,"课程已选");
        }

        // 插入选课记录
        CourseStudent sc = new CourseStudent();
        sc.setStudentId(studentByStuId.getId());
        sc.setCourseId(courseId);
        sc.setSelectTime(new Date());
        studentCourseMapper.insert(sc);

        return Result.success(REQUEST_SUCCESS,"选课成功");
    }

    @Override
    public Result getSelectedCourse() {
        User byId = userService.getById(UserHolder.getUser().getId());
        String studentId = byId.getUserName();
        Student studentByStuId = studentMapper.getStudentByStuId(studentId);

        // 查询已选课程
        QueryWrapper<CourseStudent> query = new QueryWrapper<>();
        query.eq("student_id", studentByStuId.getId());
        List<CourseStudent> selectedList = null;
        try {
            selectedList = studentCourseMapper.selectList(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (selectedList == null)
        {
            return Result.success(REQUEST_SUCCESS,Collections.emptyList());
        }

        // 获取课程详细信息
        List<Integer> courseIds = selectedList.stream()
                .map(CourseStudent::getCourseId)
                .collect(Collectors.toList());

        if (courseIds.isEmpty()) {
            return Result.success(REQUEST_SUCCESS,Collections.emptyList());
        }

        List<Course> courses = listByIds(courseIds);

        // 计算总学分
        BigDecimal totalCredit = courses.stream()
                .map(Course::getCredit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> data = new HashMap<>();
        data.put("courses", courses);
        data.put("totalCredit", totalCredit);

        return Result.success(REQUEST_SUCCESS,data);
    }

    @Override
    public Result deselectCourse(Integer courseId) {
        // 获取当前登录用户
        User byId = userService.getById(UserHolder.getUser().getId());
        String studentId = byId.getUserName();
        if (studentId == null) {
            return Result.error("未登录");
        }

        // 根据学号获取学生实体
        Student studentByStuId = studentMapper.getStudentByStuId(studentId);
        if (studentByStuId == null) {
            return Result.error("学生信息不存在");
        }

        // 查询是否存在选课记录
        QueryWrapper<CourseStudent> query = new QueryWrapper<>();
        query.eq("student_id", studentByStuId.getId())
                .eq("course_id", courseId);
        CourseStudent exist = studentCourseMapper.selectOne(query);

        if (exist == null) {
            return Result.error("未选该课程，无法退选");
        }

        // 删除选课记录
        int deleted = studentCourseMapper.delete(query);
        if (deleted > 0) {
            return Result.success(REQUEST_SUCCESS, "退选成功");
        } else {
            return Result.error("退选失败，请稍后重试");
        }
    }

}
