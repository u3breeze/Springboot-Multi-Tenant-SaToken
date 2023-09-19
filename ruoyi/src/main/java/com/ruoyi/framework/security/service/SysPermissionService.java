package com.ruoyi.framework.security.service;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysMenuService;
import com.ruoyi.project.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 * 
 * @author ruoyi
 */
@Component
public class SysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     * 
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user)
    {
        if (!user.getCom().isActive()) {
            throw new CustomException("账户已失效");
        }

        Set<String> roles = new HashSet<>();
        // 系统管理员拥有所有权限
        if (SecurityUtils.isSuperAdmin(user))
        {
            roles.add("super-admin");
        }

        if (SecurityUtils.isComAdmin(user))
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        if (!user.getCom().isActive()) {
            throw new CustomException("账户已失效");
        }

        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (SecurityUtils.isSuperAdmin(user))
        {
            perms.add("*:*:*");
        }
        else if (SecurityUtils.isComAdmin(user))
        {
            perms.addAll(menuService.selectMenuPermsByTempId(user.getCom().getTempId()));
        }
        else
        {
            perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
        }
        return perms;
    }
}
