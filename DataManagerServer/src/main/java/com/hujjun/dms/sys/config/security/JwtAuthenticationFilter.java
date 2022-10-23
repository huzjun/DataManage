package com.hujjun.dms.sys.config.security;

import com.hujjun.dms.sys.config.SecurityConfig;
import com.hujjun.dms.sys.entity.CheckResult;
import com.hujjun.dms.sys.entity.SysUser;
import com.hujjun.dms.sys.service.SysUserService;
import com.hujjun.dms.sys.util.JwtUtils;
import com.hujjun.dms.sys.util.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @AUTHOR hujjun
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        log.info("url:{}\ttoken:{}", request.getRequestURI(),token);
        // url在白名单里，则放行
        if (Arrays.asList(SecurityConfig.URL_WHITELIST).contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        CheckResult checkResult = JwtUtils.validateJWT(token);
        if (!checkResult.isSuccess()) {
            switch (checkResult.getErrCode()) {
                case JwtConstant.JWT_ERRCODE_NULL_CODE:
                    throw new JwtException(JwtConstant.JWT_ERRCODE_NULL);
                case JwtConstant.JWT_ERRCODE_FAIL_CODE:
                    throw new JwtException(JwtConstant.JWT_ERRCODE_FAIL);
                case JwtConstant.JWT_ERRCODE_EXPIRE_CODE:
                    throw new JwtException(JwtConstant.JWT_ERRCODE_EXPIRE);
                default:
                    throw new JwtException("其他");
            }
        }
        Claims claims = JwtUtils.parseJWT(token);
        log.info("claims:{}",claims);
        String username = claims.getSubject();
        SysUser sysUser = sysUserService.getByUsername(username);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, null,
                        userDetailsService.getUserAuthority(sysUser.getId()));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request, response);
    }
}
