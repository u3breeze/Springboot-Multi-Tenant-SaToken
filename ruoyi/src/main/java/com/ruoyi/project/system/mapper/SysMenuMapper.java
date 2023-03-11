package com.ruoyi.project.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.project.system.domain.SysMenu;

/**
 * 菜单表 数据层
 * 
 * @author ruoyi
 */
public interface SysMenuMapper
{

    List<SysMenu> listAllMenus();

    /**
     * 查询系统菜单列表
     * 
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 根据superflag获取所有菜单
     *
     * @param superFlag
     * @return
     */
    List<SysMenu> listBySuperFlag(Integer superFlag);

    /**
     * 根据用户所有权限
     * 
     * @return 权限列表
     */
    List<String> selectMenuPerms();

    /**
     * 根据用户查询系统菜单列表
     * 
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SysMenu> selectMenuListByUserId(SysMenu menu);



    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据租户的权限模版ID查询权限
     *
     * @param tempId 租户的权限模版ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByTempId(String tempId);

    /**
     * 根据用户ID查询菜单
     * @param temId 模板ID
     * 
     * @return 菜单列表
     */
    List<SysMenu> listMenusByTempId(String temId);


    List<SysMenu> listMenus4SuperAdmin();

    /**
     * 根据用户ID查询菜单
     * 
     * @param userId 用户ID
     * @param tempId 模版ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId, @Param("tempId")String tempId);

    /**
     * 根据角色ID查询菜单树信息
     * 
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Integer> selectMenuListByRoleId(Long roleId);

    /**
     * 根据菜单ID查询信息
     * 
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SysMenu selectMenuById(Long menuId);

    /**
     * 是否存在菜单子节点
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    int hasChildByMenuId(Long menuId);

    /**
     * 新增菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    int insertMenu(SysMenu menu);

    /**
     * 修改菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    int updateMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    int deleteMenuById(Long menuId);

    /**
     * 校验菜单名称是否唯一
     * 
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);


    /**
     * 获取已分配给模板的菜单ID
     * @param tempId
     * @return
     */
    List<Integer> listCheckedMenusByTempId(@Param("tempId") String tempId);

}
