package com.xuexian.webkeshe.vo;

import com.xuexian.webkeshe.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "登录响应数据")
public class LoginResponse {

    @Schema(description = "用户信息")
    private UserDTO user;

    @Schema(description = "JWT令牌")
    private String token;

    private List<String> roles;
}
