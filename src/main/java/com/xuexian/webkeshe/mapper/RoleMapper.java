package com.xuexian.webkeshe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuexian.webkeshe.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT r.role_name FROM role r " +
            "INNER JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> selectRolesByUserId(Long userId);

    @Select("SELECT id FROM role WHERE role_name = #{roleName}")
    Integer selectRoleIdByName(String roleName);

    @Select("SELECT * FROM role WHERE id = #{roleId}")
    Role selectRoleByRoleId(Integer roleId);


    // 返回所有角色列表
    @Select("SELECT id, role_name, description, create_time, update_time FROM role ORDER BY id ASC")
    List<Role> getAllRoles();

}
