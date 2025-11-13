package com.xuexian.webkeshe.mapper;

import com.xuexian.webkeshe.entity.College;
import com.xuexian.webkeshe.entity.Specialty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpecialtyMapper {
    @Select("SELECT * FROM specialty WHERE college_id = #{ccollegeId}")
    List<Specialty> findAll(Integer collegeId);

    @Select("SELECT * FROM specialty WHERE id = #{id}")
    Specialty selectById(Integer id);
}
