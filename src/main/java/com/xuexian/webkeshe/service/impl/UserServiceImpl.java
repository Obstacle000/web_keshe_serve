package com.xuexian.webkeshe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuexian.webkeshe.dto.AdminDTO;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.entity.Role;
import com.xuexian.webkeshe.entity.User;
import com.xuexian.webkeshe.mapper.RoleMapper;
import com.xuexian.webkeshe.mapper.UserMapper;
import com.xuexian.webkeshe.mapper.UserRoleMapper;
import com.xuexian.webkeshe.service.IUserService;
import com.xuexian.webkeshe.util.PasswordEncoder;
import com.xuexian.webkeshe.vo.PageVO;
import com.xuexian.webkeshe.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.xuexian.webkeshe.util.Code.REQUEST_SUCCESS;
import static com.xuexian.webkeshe.util.Code.USER_EXIST;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;


    @Override
    public List<String> getUserRoles(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }


    public void assignRole(Long userId, String roleName) {

        Integer roleId = roleMapper.selectRoleIdByName(roleName);
        userRoleMapper.insertUserRole(userId, roleId);
    }

    @Transactional
    @Override
    public Result addAdmin(AdminDTO adminDTO) {
        Integer userId = adminDTO.getId();  // 如果有id，就是编辑
        String userName = adminDTO.getName();
        String password = adminDTO.getPassword();
        Integer roleId = adminDTO.getRole();

        Role roleO = roleMapper.selectRoleByRoleId(roleId);
        if (roleO == null) {
            return Result.error(400, "角色类型错误");
        }
        String role = roleO.getRoleName();

        if (userId == null) {
            // 新增
            User existingUser = lambdaQuery()
                    .eq(User::getUserName, userName)
                    .one();
            if (existingUser != null) {
                return Result.error(USER_EXIST, "用户名已存在");
            }

            User user = new User();
            user.setUserName(userName);
            user.setPassword(PasswordEncoder.encode(password));
            save(user);

            // 分配角色
            assignRole(user.getId(), role);

            return Result.success(REQUEST_SUCCESS, "新增用户成功，角色：" + role);
        } else {
            // 编辑
            User user = getById(userId);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }

            user.setUserType(adminDTO.getRole());

            updateById(user);

            // 更新角色
            userRoleMapper.deleteByUserId(userId); // 删除旧角色权限
            try{
                assignRole(Long.valueOf(userId), role);

            }catch (Exception e){
                e.printStackTrace();
            }

            return Result.success(REQUEST_SUCCESS, "修改用户成功，角色：" + role);
        }
    }


    @Override
    public PageVO<User> getUserList(PageDTO pageDTO) {
        Page<User> page = Page.of(pageDTO.getPageNo(), pageDTO.getPageSize());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id"); // 根据 id 升序排序


        Page<User> p = page(page, queryWrapper);
        return new PageVO<User>(p.getTotal(), p.getRecords());
    }


}
