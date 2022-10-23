package com.hujjun.dms.sys.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @AUTHOR hujjun
 * @DESC 自定义用户认证异常
 */
public class AccountLockExcepAcction extends AuthenticationException {

    public AccountLockExcepAcction(String msg, Throwable t) {
        super(msg, t);
    }

    public AccountLockExcepAcction(String msg) {
        super(msg);
    }
}
