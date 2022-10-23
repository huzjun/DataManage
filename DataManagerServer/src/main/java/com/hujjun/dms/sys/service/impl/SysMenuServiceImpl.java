package com.hujjun.dms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hujjun.dms.sys.entity.SysMenu;
import com.hujjun.dms.sys.service.SysMenuService;
import com.hujjun.dms.sys.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @AUTHOR hujjun
 * @DESC 针对表【sys_menu】的数据库操作Service实现
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    @Override
    public List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList) {
        List<SysMenu> resultMenuList = new ArrayList<>();
        for (SysMenu parentMenu : sysMenuList) {
            // 寻找子节点
            for (SysMenu menu : sysMenuList) {
                if (parentMenu.getId().equals(menu.getParentId())) {
                    parentMenu.getChildren().add(menu);
                }
            }

            if (parentMenu.getParentId() == 0L) {
                resultMenuList.add(parentMenu);
            }
        }
        return resultMenuList;
    }
}




