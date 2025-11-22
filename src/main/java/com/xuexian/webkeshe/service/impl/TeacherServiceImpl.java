package com.xuexian.webkeshe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.entity.*;
import com.xuexian.webkeshe.mapper.*;
import com.xuexian.webkeshe.service.ITeacherService;
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
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private IUserService userService;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public PageVO<Teacher> getTeacherList(PageDTO pageDTO) {
        Page<Teacher> page = Page.of(pageDTO.getPageNo(), pageDTO.getPageSize());

        Page<Teacher> p = page(page, new QueryWrapper<Teacher>().orderByAsc("id"));

        // 手动补充名称
        for (Teacher t : p.getRecords()) {

            if (t.getCollegeId() != null) {
                College c = collegeMapper.selectById(t.getCollegeId());
                if (c != null) t.setCollegeName(c.getCollegeName());
            }
            if (t.getSpecialtyId() != null) {
                Specialty sp = specialtyMapper.selectById(t.getSpecialtyId());
                if (sp != null) t.setSpecialtyName(sp.getSpecialtyName());
            }

        }

            List<Teacher> filteredList = p.getRecords().stream()
                .filter(t -> {
                    User user = userMapper.findByUserName(t.getTeacherId());
                    return user != null && user.getStatus() == 1;
                })
                .collect(Collectors.toList());

        return new PageVO<>((long) filteredList.size(), filteredList);
    }

    @Override
    public Result addTeacher(Teacher teacher) {
        if (teacher == null || teacher.getName() == null || teacher.getTeacherId() == null) {
            return Result.error("教师信息不完整");
        }

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacher.getTeacherId());
        if (count(queryWrapper) > 0) {
            return Result.error("该工号已存在");
        }

        teacher.setCreateTime(LocalDateTime.now());
        teacher.setUpdateTime(LocalDateTime.now());





        return createStudentAccount(teacher);
    }

    @Override
    public Result updateTeacher(Teacher teacher) {
        if (teacher == null || teacher.getTeacherId() == null) {
            return Result.error("参数错误");
        }

        String teacherId = teacher.getTeacherId();
        Teacher t= null;
        try {
            t = teacherMapper.getTeacherByTeacherId(teacherId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (t == null) {
            return Result.error("找不到该教师");
        }

        t.setName(teacher.getName());


        t.setCollegeId(teacher.getCollegeId());
        t.setSpecialtyId(teacher.getSpecialtyId());
        t.setAge(teacher.getAge());
        t.setSex(teacher.getSex());
        if(teacher.getPhoneNo() != null) {
            t.setPhoneNo(teacher.getPhoneNo());
        }
        updateById(t);

        return Result.success(REQUEST_SUCCESS,"教师更新成功");
    }

    @Override
    @Transactional
    public void removeBatchByIds(List<Integer> ids) {
        userMapper.disableUsersByTeacherIds(
                ids.stream().map(String::valueOf).collect(Collectors.joining(","))
        );
    }

    public Result<Object> createStudentAccount(Teacher teacher) {

        String teacherId = teacher.getTeacherId();

        User existingUser = userService.lambdaQuery()
                .eq(User::getUserName, teacherId)
                .one();
        if (existingUser != null) {
            return Result.error(USER_EXIST, "用户名已存在");
        }

        User user = new User();

        user.setPassword(PasswordEncoder.encode("123456"));

        user.setUserType(3);

        user.setUserName(teacherId);

        userService.save(user);
        teacher.setUserId(user.getId());
        boolean save = this.save(teacher);
        if (!save) {
            return Result.error("添加教师失败");
        }

        assignRole(user.getId(),"TEACHER");


        return Result.success(REQUEST_SUCCESS,"教师添加成功");

    }
    public void assignRole(Integer userId, String roleName) {

        Integer roleId = roleMapper.selectRoleIdByName(roleName);
        userRoleMapper.insertUserRole(userId, roleId);
    }

}
