package com.xuexian.webkeshe.mapper;

import com.xuexian.webkeshe.entity.College;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollegeMapper {
    @Select("SELECT * FROM college ")
    List<College> findAll();

    @Select("SELECT * FROM college WHERE id = #{id}")
    College selectById(Integer id);
}
