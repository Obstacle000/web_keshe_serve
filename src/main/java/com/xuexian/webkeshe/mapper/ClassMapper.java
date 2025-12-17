package com.xuexian.webkeshe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuexian.webkeshe.entity.ClassInfo;
import com.xuexian.webkeshe.entity.College;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassMapper extends BaseMapper<ClassInfo> {

    @Select("SELECT * FROM class WHERE specialty_id = #{specialtyId}")
    List<ClassInfo> findAll(Integer specialtyId);

    @Select("SELECT * FROM class WHERE id = #{id}")
    ClassInfo selectById(Integer id);
}
