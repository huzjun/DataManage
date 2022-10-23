package com.hujjun.dms.sys.config.security;

import com.hujjun.dms.sys.config.exception.AccountLockExcepAcction;
import com.hujjun.dms.sys.entity.SysUser;
import com.hujjun.dms.sys.service.SysUserService;
import com.hujjun.dms.sys.util.constant.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @AUTHOR hujjun
 * @DESC 自定义 UserDetails
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException(AuthConstant.USERNAME_PASSWORD_NOT_MATCH);
        } else if (AuthConstant.ACCOUNT_DISABLE_CODE.equals(sysUser.getStatus())) {
            throw new AccountLockExcepAcction(AuthConstant.ACCOUNT_DISABLE);
        }
        return new User(sysUser.getUsername(), sysUser.getPassword(), getUserAuthority(sysUser.getId()));
    }


    public List<GrantedAuthority> getUserAuthority(Long userId) {
        String authority = sysUserService.getUserAuthorityInfo(userId);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
