package com.ruoyi.project.system.controller;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.SysMenu;
import com.ruoyi.project.system.service.ISysMenuService;

/**
 * 菜单信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

  @Autowired
  private ISysMenuService menuService;

  @Autowired
  private TokenService tokenService;

  /**
   * 获取菜单列表
   */
  @SaCheckPermission("system:menu:list")
  @GetMapping("/list")
  public AjaxResult list(SysMenu menu) {
    List<SysMenu> menus = menuService.selectMenuList(menu, SecurityUtils.getSysUser());
    return AjaxResult.success(menus);
  }

  /**
   * 根据菜单编号获取详细信息
   */
  @SaCheckPermission("system:menu:query")
  @GetMapping(value = "/{menuId}")
  public AjaxResult getInfo(@PathVariable Long menuId) {
    return AjaxResult.success(menuService.selectMenuById(menuId));
  }

  /**
   * 获取菜单下拉树列表
   */
  @GetMapping("/treeselect")
  public AjaxResult treeselect(SysMenu menu) {
    List<SysMenu> menus = menuService.selectMenuList(menu, SecurityUtils.getSysUser());
    return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
  }

  /**
   * 加载对应角色菜单列表树
   */
  @GetMapping(value = "/roleMenuTreeselect/{roleId}")
  public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
    List<SysMenu> menus = menuService.selectMenuList(SecurityUtils.getSysUser());
    AjaxResult ajax = AjaxResult.success();
    ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
    ajax.put("menus", menuService.buildMenuTreeSelect(menus));
    return ajax;
  }

  /**
   * 新增菜单
   */
  @SaCheckPermission("system:menu:add")
  @Log(title = "菜单管理", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@Validated @RequestBody SysMenu menu) {
    if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
      return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
    } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
        && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
      return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
    }
    menu.setCreateBy(SecurityUtils.getUsername());
    return toAjax(menuService.insertMenu(menu));
  }

  /**
   * 修改菜单
   */
  @SaCheckPermission("system:menu:edit")
  @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
    if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
      return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
    } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
        && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
      return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
    }
    menu.setUpdateBy(SecurityUtils.getUsername());
    return toAjax(menuService.updateMenu(menu));
  }

  /**
   * 删除菜单
   */
  @SaCheckPermission("system:menu:remove")
  @Log(title = "菜单管理", businessType = BusinessType.DELETE)
  @DeleteMapping("/{menuId}")
  public AjaxResult remove(@PathVariable("menuId") Long menuId) {
    if (menuService.hasChildByMenuId(menuId)) {
      return AjaxResult.error("存在子菜单,不允许删除");
    }
    if (menuService.checkMenuExistRole(menuId)) {
      return AjaxResult.error("菜单已分配,不允许删除");
    }
    return toAjax(menuService.deleteMenuById(menuId));
  }


  /**
   * 系统超级管理员查询出所有的菜单信息
   * 用于分配菜单，所以此处只能展示非超管菜单用于分配
   */
  @GetMapping(value = "/allMenus/{tempId}")
  public AjaxResult allMenus(@PathVariable("tempId") String tempId) {
//    checkSuperAdmin();
    List<SysMenu> menus = menuService.listMenusBySuperFlag(Constants.FLAG_NO);
    AjaxResult ajax = AjaxResult.success();
    // 对应模板已分配的菜单
    ajax.put("checkedKeys", menuService.listCheckedMenusByTempId(tempId));
    // 平台超级管理员才可以查看所有菜单
    ajax.put("menus", menuService.buildMenuTreeSelect(menus));
    return ajax;
  }
}