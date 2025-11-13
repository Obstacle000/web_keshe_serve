package com.xuexian.webkeshe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuexian.webkeshe.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE user_name = #{userName}")
    User findByUserName(String userName);

    @Update("""
    UPDATE user 
    SET status = 0 
    WHERE user_name IN (
        SELECT stu_id FROM student WHERE id IN (${ids})
    )
""")
    void disableUsersByStudentIds(@Param("ids") String ids);

}
