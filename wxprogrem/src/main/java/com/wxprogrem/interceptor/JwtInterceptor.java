package com.wxprogrem.interceptor;

import com.wxprogrem.utils.Jwtutils;
import com.wxprogrem.utils.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 处理 OPTIONS 请求，允许跨域预检请求通过
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        System.out.println("收到的token：" + token); // 打印token
        if (token == null) {
            sendErrorResponse(response, Result.error("token验证失败"));
            System.out.print("token验证失败，拦截");
            return false;
        }
        // 2. 获取请求路径和方法
        String url = request.getRequestURI();
        String method = request.getMethod();
        // 3. 开放优惠券查询接口
        if (url.startsWith("/voucher") && "GET".equalsIgnoreCase(method)) {
            return true;  // 不验证 token
        }
        if (url.startsWith("/user/voucher")) {
            return true;
        }
        String realToken = token.substring(7);
        try {
            Jwtutils.parseToken(realToken);
            return true;
        } catch (Exception e) {
            // 记录详细异常信息
            System.err.println("JWT 验证失败: " + e.getMessage());
            sendErrorResponse(response, Result.error("无效的token"));
            return false;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, Result result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(result.toString());
    }
}