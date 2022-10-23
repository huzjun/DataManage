package com.hujjun.dms.sys.config.security;

import cn.hutool.json.JSONUtil;
import com.hujjun.dms.sys.entity.Result;
import com.hujjun.dms.sys.util.constant.AuthConstant;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @AUTHOR hujjun
 * @DATE 2022/10/23
 * @DESC Jwt认证失败处理
 */


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        outputStream.write(JSONUtil.toJsonStr(Result.error(HttpServletResponse.SC_UNAUTHORIZED, AuthConstant.AUTHENTICATION_FAILED)).getBytes());
        outputStream.flush();
        outputStream.close();
    }
}

