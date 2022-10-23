package com.hujjun.jwtshiro.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

import java.util.UUID;

/**
 * @AUTHOR hujjun
 * @DATE 2022/10/10
 * @DESC
 */


public class JwtToken extends UsernamePasswordToken {
    private String token;

    public JwtToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
        this.token = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

}
