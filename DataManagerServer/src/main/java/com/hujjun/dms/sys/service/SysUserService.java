package com.hujjun.dms.sys.service;

import com.hujjun.dms.sys.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @AUTHOR hujjun
 * @DESC 针对表【sys_user】的数据库操作Service
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    SysUser getByUsername(String username);

    String getUserAuthorityInfo(Long userId);
}
