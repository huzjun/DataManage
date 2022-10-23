package com.hujjun.dms.controller;

import com.hujjun.dms.entity.Result;
import com.hujjun.dms.entity.SysUser;
import com.hujjun.dms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试
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
    public Result userList(){
        Map<String,Object> resutlMap=new HashMap<>();
        List<SysUser> userList = sysUserService.list();
        resutlMap.put("userList",userList);
        return Result.ok(resutlMap);
    }
}
