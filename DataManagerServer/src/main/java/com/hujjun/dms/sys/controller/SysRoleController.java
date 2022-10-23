package com.hujjun.dms.sys.controller;

import com.hujjun.dms.sys.service.SysRoleService;
import com.hujjun.dms.sys.entity.Result;
import com.hujjun.dms.sys.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @AUTHOR hujjun
 * @DESC 系统角色Controller控制器
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/listAll")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result listAll(){
        Map<String,Object> resultMap=new HashMap<>();
        List<SysRole> roleList = sysRoleService.list();
        resultMap.put("roleList",roleList);
        return Result.ok(resultMap);
    }
}