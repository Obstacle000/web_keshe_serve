package com.xuexian.webkeshe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuexian.webkeshe.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Select("SELECT * FROM student WHERE stu_id = #{stuId}")
    Student getStudentByStuId(String stuId);
}
