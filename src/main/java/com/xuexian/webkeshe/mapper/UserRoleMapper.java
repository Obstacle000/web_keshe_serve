package com.xuexian.webkeshe.mapper;

import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    @Insert("INSERT INTO user_role(user_id, role_id) VALUES(#{userId}, #{roleId})")
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Integer roleId);
    @Delete("DELETE FROM user_role WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Integer userId);
}
