package com.hujjun.dms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * @AUTHOR hujjun
 */
@TableName(value = "sys_role")
@Data
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 角色权限字符串
     */
    @TableField(value = "code")
    private String code;


}