package com.hujjun.dms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hujjun.dms.sys.entity.*;
import com.hujjun.dms.sys.service.SysRoleService;
import com.hujjun.dms.sys.service.SysUserRoleService;
import com.hujjun.dms.sys.service.SysUserService;
import com.hujjun.dms.sys.util.DateUtil;
import com.hujjun.dms.sys.util.StringUtil;
import com.hujjun.dms.sys.util.constant.AuthConstant;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * @AUTHOResult hujjun
 * @DESC 用户Controller控制器
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService SysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleService SysUserRoleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${avatarImagesFilePath}")
    private String avatarImagesFilePath;

    /**
     * 添加或者修改
     *
     * @param SysUser
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:user:add')" + "||" + "hasAuthority('system:user:edit')")
    public Result save(@RequestBody SysUser SysUser) {
        if (SysUser.getId() == null || SysUser.getId() == -1) {
            SysUser.setCreateTime(new Date());
            SysUser.setPassword(bCryptPasswordEncoder.encode(SysUser.getPassword()));
            SysUserService.save(SysUser);
        } else {
            SysUser.setUpdateTime(new Date());
            SysUserService.updateById(SysUser);
        }
        return Result.ok();
    }

    /**
     * 修改密码
     *
     * @param SysUser
     * @return
     */
    @PostMapping("/updateUserPwd")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result updateUserPwd(@RequestBody SysUser SysUser) {
        SysUser currentUser = SysUserService.getById(SysUser.getId());
        if (bCryptPasswordEncoder.matches(SysUser.getOldPassword(), currentUser.getPassword())) {
            currentUser.setPassword(bCryptPasswordEncoder.encode(SysUser.getNewPassword()));
            currentUser.setUpdateTime(new Date());
            SysUserService.updateById(currentUser);
            return Result.ok();
        } else {
            return Result.error("输入旧密码错误！");
        }
    }

    /**
     * 上传用户头像图片
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadImage")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Map<String, Object> uploadImage(MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (!file.isEmpty()) {
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = DateUtil.getCurrentDateStr() + suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(avatarImagesFilePath + newFileName));
            resultMap.put("code", 0);
            resultMap.put("msg", "上传成功");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("title", newFileName);
            dataMap.put("src", "image/userAvatar/" + newFileName);
            resultMap.put("data", dataMap);
        }
        return resultMap;
    }

    /**
     * 修改用户头像
     *
     * @param SysUser
     * @return
     */
    @RequestMapping("/updateAvatar")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result updateAvatar(@RequestBody SysUser SysUser) {
        SysUser currentUser = SysUserService.getById(SysUser.getId());
        currentUser.setUpdateTime(new Date());
        currentUser.setAvatar(SysUser.getAvatar());
        SysUserService.updateById(currentUser);
        return Result.ok();
    }

    /**
     * 根据条件分页查询用户信息
     *
     * @param pageBean
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result list(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        Page<SysUser> pageResult = SysUserService.page(new Page<>(pageBean.getPageNum(), pageBean.getPageSize()), new QueryWrapper<SysUser>().like(StringUtil.isNotEmpty(query), "username", query));
        List<SysUser> userList = pageResult.getRecords();
        for (SysUser user : userList) {
            List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id=" + user.getId()));
            user.setSysRoleList(roleList);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userList", userList);
        resultMap.put("total", pageResult.getTotal());
        return Result.ok(resultMap);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result findById(@PathVariable(value = "id") Integer id) {
        SysUser SysUser = SysUserService.getById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("SysUser", SysUser);
        return Result.ok(map);
    }

    /**
     * 验证用户名
     *
     * @param SysUser
     * @return
     */
    @PostMapping("/checkUserName")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result checkUserName(@RequestBody SysUser SysUser) {
        if (SysUserService.getByUsername(SysUser.getUsername()) == null) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public Result delete(@RequestBody Long[] ids) {
        SysUserService.removeByIds(Arrays.asList(ids));
        SysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));
        return Result.ok();
    }

    /**
     * 重置密码
     *
     * @param id
     * @return
     */
    @GetMapping("/resetPassword/{id}")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result resetPassword(@PathVariable(value = "id") Integer id) {
        SysUser SysUser = SysUserService.getById(id);
        SysUser.setPassword(bCryptPasswordEncoder.encode(AuthConstant.DEFAULT_PASSWORD));
        SysUser.setUpdateTime(new Date());
        SysUserService.updateById(SysUser);
        return Result.ok();
    }

    /**
     * 更新status状态
     *
     * @param id
     * @param status
     * @return
     */
    @GetMapping("/updateStatus/{id}/status/{status}")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result updateStatus(@PathVariable(value = "id") Integer id, @PathVariable(value = "status") String status) {
        SysUser SysUser = SysUserService.getById(id);
        SysUser.setStatus(status);
        SysUserService.saveOrUpdate(SysUser);
        return Result.ok();
    }

    /**
     * 用户角色授权
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Transactional
    @PostMapping("/grantRole/{userId}")
    @PreAuthorize("hasAuthority('system:user:role')")
    public Result grantRole(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {
        List<SysUserRole> useroleList = new ArrayList<>();
        Arrays.stream(roleIds).forEach(r -> {
            SysUserRole SysUserRole = new SysUserRole();
            SysUserRole.setRoleId(r);
            SysUserRole.setUserId(userId);
            useroleList.add(SysUserRole);
        });
        SysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        SysUserRoleService.saveBatch(useroleList);
        return Result.ok();
    }
}
