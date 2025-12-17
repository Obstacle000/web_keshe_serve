package com.xuexian.webkeshe.mapper;

import com.xuexian.webkeshe.entity.Specialty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpecialtyMapper {
    @Select("SELECT * FROM specialty WHERE college_id = #{collegeId}")
    List<Specialty> findAll(Integer collegeId);

    @Select("SELECT * FROM specialty WHERE id = #{id}")
    Specialty selectById(Integer id);
    @Select("SELECT * FROM specialty")
    List<Specialty> getList();
}
