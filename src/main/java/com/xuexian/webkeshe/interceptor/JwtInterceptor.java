package com.xuexian.webkeshe.interceptor;

import com.aliyun.oss.HttpMethod;
import com.xuexian.webkeshe.dto.UserDTO;
import com.xuexian.webkeshe.util.JwtUtil;
import com.xuexian.webkeshe.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("JWT拦截器 path=" + request.getRequestURI());

        //如果请求为 OPTIONS 请求(还真是,内个权限列表)，则返回 true,否则需要通过jwt验证
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            System.out.println("OPTIONS请求，放行");
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null ) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("缺少token");
            return false;
        }
        System.out.println("存在token");

        try {

            var claims = JwtUtil.parseToken(token);

            UserDTO user = new UserDTO(
                    Integer.parseInt(claims.getId()),
                    claims.getSubject(),
                    claims.get("role", Integer.class)
            );
            UserHolder.saveUser(user);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("token无效或过期");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserHolder.removeUser();
    }
}
