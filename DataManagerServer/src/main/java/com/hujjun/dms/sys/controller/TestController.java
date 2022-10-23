package com.hujjun.dms.sys.controller;

import com.hujjun.dms.sys.entity.Result;
import com.hujjun.dms.sys.entity.SysUser;
import com.hujjun.dms.sys.service.SysUserService;
import com.hujjun.dms.sys.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试
 *
 * @author java1234_小锋 （公众号：java1234）
 * @site www.java1234.vip
 * @company 南通小锋网络科技有限公司
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/user/list")
    // @PreAuthorize("hasRole('ROLE_admin2')")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result userList(@RequestHeader(required = false) String token) {
        if (StringUtil.isNotEmpty(token)) {
            Map<String, Object> resutlMap = new HashMap<>();
            List<SysUser> userList = sysUserService.list();
            resutlMap.put("userList", userList);
            return Result.ok(resutlMap);
        } else {
            return Result.error(401, "没有权限访问");
        }

    }
}
