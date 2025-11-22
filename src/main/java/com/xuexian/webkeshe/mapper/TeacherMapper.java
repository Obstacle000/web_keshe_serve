package com.xuexian.webkeshe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuexian.webkeshe.entity.Student;
import com.xuexian.webkeshe.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    @Select("SELECT * FROM teacher WHERE teacher_id = #{teacherId}")
    Teacher getTeacherByTeacherId(String teacherId);
}
