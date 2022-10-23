package com.hujjun.dms.sys.config.security;

import cn.hutool.json.JSONUtil;
import com.hujjun.dms.sys.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @AUTHOR hujjun
 * @DESC 自定义logout处理
 */
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        outputStream.write(JSONUtil.toJsonStr(Result.ok("登出成功")).getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
