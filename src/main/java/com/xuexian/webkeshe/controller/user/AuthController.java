package com.xuexian.webkeshe.controller.user;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xuexian.webkeshe.dto.LoginFormDTO;
import com.xuexian.webkeshe.dto.UserDTO;
import com.xuexian.webkeshe.entity.User;
import com.xuexian.webkeshe.mapper.RoleMapper;
import com.xuexian.webkeshe.service.IRoleService;
import com.xuexian.webkeshe.service.IUserService;
import com.xuexian.webkeshe.util.JwtUtil;
import com.xuexian.webkeshe.util.PasswordEncoder;

import com.xuexian.webkeshe.vo.LoginResponse;
import com.xuexian.webkeshe.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.xuexian.webkeshe.util.Code.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@Tag(name = "登录接口", description = "处理统一认证和自定义登录业务")
public class AuthController {

    @Autowired
    private IUserService userService;
    @Autowired
    private RoleMapper roleMapper;



    @Operation(summary = "用户登录接口", description = "通过用户名和密码登录，返回JWT和用户信息")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功"),
            @ApiResponse(responseCode = "-10000", description = "用户不存在"),
            @ApiResponse(responseCode = "-10001", description = "密码错误")
    })
    @PostMapping("/login")
    public Result<LoginResponse> login(
            @Parameter(description = "JSON格式接受userName和password")
            @RequestBody LoginFormDTO loginForm
    ) {
        User user = userService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, loginForm.getUserName()));

        if (user == null) {
            return Result.error(USER_NOT_EXIST, "用户不存在");
        }

        if (!PasswordEncoder.matches( loginForm.getPassword(),user.getPassword())) {
            return Result.error(PASSWORD_ERROR, "密码错误");
        }

        User u = userService.query().eq("user_name", loginForm.getUserName()).one();

        UserDTO userDTO = new UserDTO(user.getId(), user.getUserName(), u.getUserType());
        userDTO.setRole(u.getUserType());

        String jwt = JwtUtil.generateToken(user.getId(), user.getUserName(), userDTO.getRoles());
        log.info("jwt:{}", jwt);

        LoginResponse data = new LoginResponse();
        data.setUser(userDTO);
        data.setToken(jwt);

        return Result.success(REQUEST_SUCCESS, data);
    }
}
