package com.xuexian.webkeshe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.entity.*;
import com.xuexian.webkeshe.mapper.*;
import com.xuexian.webkeshe.service.IStudentService;
import com.xuexian.webkeshe.service.IUserService;
import com.xuexian.webkeshe.util.PasswordEncoder;
import com.xuexian.webkeshe.vo.PageVO;
import com.xuexian.webkeshe.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.xuexian.webkeshe.util.Code.REQUEST_SUCCESS;
import static com.xuexian.webkeshe.util.Code.USER_EXIST;
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private IUserService userService;
    @Autowired
    private StudentMapper studentMapper;


    @Override
    public PageVO<Student> getStudentList(PageDTO pageDTO) {
        Page<Student> page = Page.of(pageDTO.getPageNo(), pageDTO.getPageSize());

        Page<Student> p = page(page, new QueryWrapper<Student>().orderByAsc("id"));

        // 手动补充名称
        for (Student stu : p.getRecords()) {

            if (stu.getCollegeId() != null) {
                College c = collegeMapper.selectById(stu.getCollegeId());
                if (c != null) stu.setCollegeName(c.getCollegeName());
            }
            if (stu.getSpecialtyId() != null) {
                Specialty sp = specialtyMapper.selectById(stu.getSpecialtyId());
                if (sp != null) stu.setSpecialtyName(sp.getSpecialtyName());
            }
            if (stu.getClassId() != null) {
                ClassInfo cl = classMapper.selectById(stu.getClassId());
                if (cl != null) stu.setClassName(cl.getClassName());
            }
        }

        List<Student> filteredList = p.getRecords().stream()
                .filter(stu -> {
                    User user = userMapper.findByUserName(stu.getStuId());
                    return user != null && user.getStatus() == 1;
                })
                .collect(Collectors.toList());

        return new PageVO<>((long) filteredList.size(), filteredList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addStu(Student student) {

        if (student == null || student.getName() == null || student.getStuId() == null) {
            return Result.error("学生信息不完整");
        }

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stu_id", student.getStuId());
        if (count(queryWrapper) > 0) {
            return Result.error("该学号已存在");
        }

        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());

        boolean save = this.save(student);
        if (!save) {
            return Result.error("添加学生失败");
        }



        return createStudentAccount(student);
    }

    @Override
    @Transactional
    public void removeBatchByIds(List<Integer> ids) {
        userMapper.disableUsersByStudentIds(
                ids.stream().map(String::valueOf).collect(Collectors.joining(","))
        );

    }

    @Override
    @Transactional
    public Result updateStu(Student student) {


        if (student == null || student.getStuId() == null) {
            return Result.error("参数错误");
        }

        String stuId = student.getStuId();
        Student s = null;
        try {
            s = studentMapper.getStudentByStuId(stuId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (s == null) {
            return Result.error("找不到该学生");
        }
        s.setAddress(student.getAddress());
        s.setName(student.getName());

        s.setClassId(student.getClassId());
        s.setCollegeId(student.getCollegeId());
        s.setSpecialtyId(student.getSpecialtyId());
        s.setAge(student.getAge());
        s.setSex(student.getSex());
        if(student.getPhoneNo() != null) {
            s.setPhoneNo(student.getPhoneNo());
        }
        updateById(s);
        return Result.success(REQUEST_SUCCESS,"学生更新成功");


    }

    public Result<Object> createStudentAccount(Student student) {

        String stuId = student.getStuId();

        User existingUser = userService.lambdaQuery()
                .eq(User::getUserName, stuId)
                .one();
        if (existingUser != null) {
            return Result.error(USER_EXIST, "用户名已存在");
        }

        User user = new User();

        user.setPassword(PasswordEncoder.encode("123456"));

        user.setUserType(1);

        user.setUserName(stuId);

        userService.save(user);

        return Result.success(REQUEST_SUCCESS,"学生添加成功");

    }
}
