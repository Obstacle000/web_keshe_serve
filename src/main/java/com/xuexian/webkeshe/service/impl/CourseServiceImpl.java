package com.xuexian.webkeshe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuexian.webkeshe.dto.CourseDTO;
import com.xuexian.webkeshe.dto.getCourseDTO;
import com.xuexian.webkeshe.dto.PageDTO;
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
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.xuexian.webkeshe.util.Code.COURSE_NOT_EXIT;
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
        String name = courseDTO.getCourseName();
        String code = courseDTO.getCourseCode();

        // 构造查询条件
        QueryWrapper<Course> query = new QueryWrapper<>();
        if (collegeId != null) {
            query.eq("college_id", collegeId);
        }
        if (name != null && !name.isEmpty()) {
            query.like("course_name", name);
        }
        if (code != null && !code.isEmpty()) {
            query.like("course_code", code);
        }


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

    public Result addCourse(CourseDTO courseDTO) {
        try{
            if(courseDTO == null){
                return Result.error("课程信息不能为空");
            }
            if (courseDTO.getCourseName() == null) {
                return Result.error("课程名称不能为空");
            }
            if (courseDTO.getCourseCode() == null) {
                return Result.error("课程编号不能为空");
            }
            if (courseDTO.getCredit() == null || courseDTO.getCredit().compareTo(BigDecimal.ZERO) <= 0) {
                return Result.error("学分必须大于0");
            }
            if (courseDTO.getTeacherId() == null) {
                return Result.error("任课教师不能为空");
            }
            if (courseDTO.getCollegeId() == null) {
                return Result.error("所属学院不能为空");
            }
            if (courseDTO.getTime() == null){
                return Result.error("上课时间不能为空");
            }

            // 2. 检查课程编号是否重复
            QueryWrapper<Course> codeQuery = new QueryWrapper<>();
            codeQuery.eq("course_code", courseDTO.getCourseCode());
            Course existCourse = getOne(codeQuery);
            if (existCourse != null) {
                return Result.error("课程编号已存在");
            }

            // 3. 检查教师是否存在
            Teacher teacher = teacherService.getById(courseDTO.getTeacherId());
            if (teacher == null) {
                return Result.error("指定的教师不存在");
            }

            // 4. 检查学院是否存在
            College college = collegeMapper.selectById(courseDTO.getCollegeId());
            if (college == null) {
                return Result.error("指定的学院不存在");
            }

            // 5. 课程时间验证
            if (courseDTO.getTime() < 1 || courseDTO.getTime() > 12) {
                return Result.error("课程时间必须在1-12节之间");
            }
            // 检查时间冲突：同一教师在同一时间不能有多个课程
            QueryWrapper<Course> timeConflictQuery = new QueryWrapper<>();
            timeConflictQuery.eq("teacher_id", courseDTO.getTeacherId())
                    .eq("time", courseDTO.getTime())
                    .eq("status", 1); // 只检查正常状态的课程
            Course conflictCourse = getOne(timeConflictQuery);
            if (conflictCourse != null) {
                teacher = teacherService.getById(courseDTO.getTeacherId());
                return Result.error(String.format("教师%s在第%d节已有课程《%s》，时间冲突",
                        teacher != null ? teacher.getName() : "未知", courseDTO.getTime(), conflictCourse.getCourseName()));
            }

            // DTO转Entity
            Course course = new Course();
            course.setCourseName(courseDTO.getCourseName());
            course.setCourseCode(courseDTO.getCourseCode());
            course.setCredit(courseDTO.getCredit());
            course.setTeacherId(courseDTO.getTeacherId());
            course.setCollegeId(courseDTO.getCollegeId());
            course.setStatus(courseDTO.getStatus() != null ? courseDTO.getStatus() : 1);
            course.setTime(courseDTO.getTime());
            course.setCreateTime(LocalDateTime.now());
            course.setUpdateTime(LocalDateTime.now());

            boolean saved = save(course);
            if (saved) {
                return Result.success(REQUEST_SUCCESS, "课程添加成功");
            } else {
                return Result.error("课程添加失败");
            }
        }
        catch (Exception e){
            log.error("添加课程异常", e);
            return Result.error("添加课程失败，请稍后重试");
        }

    }

    public Result updateCourse(CourseDTO courseDTO) {
        try {
            // 1. 参数校验
            if (courseDTO == null || courseDTO.getId() == null) {
                return Result.error("课程ID不能为空");
            }

            // 2. 检查课程是否存在
            Course existingCourse = getById(courseDTO.getId());
            if (existingCourse == null) {
                return Result.error("课程不存在");
            }

            // 3. 检查课程名称是否为空（如果提供了课程名称）
            if (StringUtils.hasText(courseDTO.getCourseName())) {
                if (courseDTO.getCourseName().trim().isEmpty()) {
                    return Result.error("课程名称不能为空");
                }
            }

            // 4. 如果修改了课程编号，检查是否重复
            if (StringUtils.hasText(courseDTO.getCourseCode()) &&
                    !courseDTO.getCourseCode().equals(existingCourse.getCourseCode())) {
                QueryWrapper<Course> codeQuery = new QueryWrapper<>();
                codeQuery.eq("course_code", courseDTO.getCourseCode())
                        .ne("id", courseDTO.getId());
                Course duplicateCourse = getOne(codeQuery);
                if (duplicateCourse != null) {
                    return Result.error("课程编号已存在");
                }
            }

            // 5. 学分验证（如果提供了学分）
            if (courseDTO.getCredit() != null) {
                if (courseDTO.getCredit().compareTo(BigDecimal.ZERO) <= 0) {
                    return Result.error("学分必须大于0");
                }
            }

            // 6. 检查教师是否存在（如果修改了教师）
            if (courseDTO.getTeacherId() != null && !courseDTO.getTeacherId().equals(existingCourse.getTeacherId())) {
                Teacher teacher = teacherService.getById(courseDTO.getTeacherId());
                if (teacher == null) {
                    return Result.error("指定的教师不存在");
                }
            }

            // 7. 检查学院是否存在（如果修改了学院）
            if (courseDTO.getCollegeId() != null && !courseDTO.getCollegeId().equals(existingCourse.getCollegeId())) {
                College college = collegeMapper.selectById(courseDTO.getCollegeId());
                if (college == null) {
                    return Result.error("指定的学院不存在");
                }
            }

            // 8. 课程时间验证（如果提供了课程时间）
            if (courseDTO.getTime() != null) {
                if (courseDTO.getTime() < 1 || courseDTO.getTime() > 12) {
                    return Result.error("课程时间必须在1-12节之间");
                }

                // 检查时间冲突：同一教师在同一时间不能有多个课程
                if (courseDTO.getTeacherId() != null || courseDTO.getTime() != existingCourse.getTime()) {
                    Integer teacherId = courseDTO.getTeacherId() != null ? courseDTO.getTeacherId() : existingCourse.getTeacherId();
                    Integer time = courseDTO.getTime() != null ? courseDTO.getTime() : existingCourse.getTime();

                    QueryWrapper<Course> timeConflictQuery = new QueryWrapper<>();
                    timeConflictQuery.eq("teacher_id", teacherId)
                            .eq("time", time)
                            .eq("status", 1) // 只检查正常状态的课程
                            .ne("id", courseDTO.getId()); // 排除当前课程

                    Course conflictCourse = getOne(timeConflictQuery);
                    if (conflictCourse != null) {
                        Teacher teacher = teacherService.getById(teacherId);
                        return Result.error(String.format("教师%s在第%d节已有课程《%s》，时间冲突",
                                teacher != null ? teacher.getName() : "未知", time, conflictCourse.getCourseName()));
                    }
                }
            }

            Course updateCourse = new Course();
            updateCourse.setId(courseDTO.getId());
            updateCourse.setUpdateTime(LocalDateTime.now());

            if (StringUtils.hasText(courseDTO.getCourseName())) {
                updateCourse.setCourseName(courseDTO.getCourseName().trim());
            }
            if (StringUtils.hasText(courseDTO.getCourseCode())) {
                updateCourse.setCourseCode(courseDTO.getCourseCode());
            }
            if (courseDTO.getCredit() != null) {
                updateCourse.setCredit(courseDTO.getCredit());
            }
            if (courseDTO.getTeacherId() != null) {
                updateCourse.setTeacherId(courseDTO.getTeacherId());
            }
            if (courseDTO.getCollegeId() != null) {
                updateCourse.setCollegeId(courseDTO.getCollegeId());
            }
            if (courseDTO.getStatus() != null) {
                updateCourse.setStatus(courseDTO.getStatus());
            }
            if (courseDTO.getTime() != null) {
                updateCourse.setTime(courseDTO.getTime());
            }

            boolean updated = updateById(updateCourse);
            if (updated) {
                return Result.success(REQUEST_SUCCESS, "课程更新成功");
            } else {
                return Result.error("课程更新失败");
            }
        } catch (Exception e) {
            log.error("更新课程异常", e);
            return Result.error("更新课程失败，请稍后重试");
        }
    }

    public Result deleteCourse(Integer courseId) {
        try {
            if (courseId == null) {
                return Result.error("课程ID不能为空");
            }

            // 1. 检查课程是否存在
            Course course = getById(courseId);
            if (course == null) {
                return Result.error("课程不存在");
            }

            // 2. 检查是否有学生选课
            QueryWrapper<CourseStudent> selectQuery = new QueryWrapper<>();
            selectQuery.eq("course_id", courseId);
            Long selectCount = studentCourseMapper.selectCount(selectQuery);
            if (selectCount > 0) {
                return Result.error("该课程已有学生选课，无法删除");
            }

            // 3. 逻辑删除（将状态改为0）
            course.setStatus(0);
            course.setUpdateTime(LocalDateTime.now());
            boolean updated = updateById(course);

            if (updated) {
                return Result.success(REQUEST_SUCCESS, "课程删除成功");
            } else {
                return Result.error("课程删除失败");
            }
        } catch (Exception e) {
            log.error("删除课程异常", e);
            return Result.error("删除课程失败，请稍后重试");
        }
    }

    public Result getCourse(Integer courseId) {
        Course course = query().eq("id", courseId).one();
        if (course == null) {
            return  Result.error("课程不存在");
        }
        return Result.success(REQUEST_SUCCESS, course);
    }
}
