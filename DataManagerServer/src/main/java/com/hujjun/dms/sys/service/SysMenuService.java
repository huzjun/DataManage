package com.hujjun.dms.sys.service;

import com.hujjun.dms.sys.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @AUTHOR hujjun
* @DESC 针对表【sys_menu】的数据库操作Service
*/
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList);
}
