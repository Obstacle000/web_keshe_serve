package com.xuexian.webkeshe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuexian.webkeshe.entity.Student;
import com.xuexian.webkeshe.entity.Teacher;
import com.xuexian.webkeshe.mapper.StudentMapper;
import com.xuexian.webkeshe.mapper.TeacherMapper;
import com.xuexian.webkeshe.service.IStudentService;
import com.xuexian.webkeshe.service.ITeacherService;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

}
