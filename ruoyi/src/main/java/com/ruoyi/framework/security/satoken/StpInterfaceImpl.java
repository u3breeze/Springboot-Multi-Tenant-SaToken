package com.ruoyi.framework.security.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.ruoyi.framework.security.service.SysPermissionService;
import com.ruoyi.framework.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 自定义权限验证接口扩展 
 */
@Component	// 打开此注解，保证此类被springboot扫描，即可完成sa-token的自定义权限验证扩展 
public class StpInterfaceImpl implements StpInterface {

	@Autowired
	private SysPermissionService permissionService;
	@Autowired
	private TokenService tokenService;

	/**
	 * 返回一个账号所拥有的权限码集合 
	 */
	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		Set<String> set = permissionService.getMenuPermission(tokenService.getLoginUser().getUser());
		return new ArrayList<>(set);
	}

	/**
	 * 返回一个账号所拥有的角色标识集合 
	 */
	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		Set<String> set = permissionService.getRolePermission(tokenService.getLoginUser().getUser());
		return new ArrayList<>(set);
	}

}
