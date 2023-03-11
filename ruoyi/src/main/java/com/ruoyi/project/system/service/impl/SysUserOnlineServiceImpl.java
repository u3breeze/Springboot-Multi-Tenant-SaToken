package com.ruoyi.project.system.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.TokenSign;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.page.PageDomain;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.monitor.domain.SysUserOnline;
import com.ruoyi.project.system.service.ISysUserOnlineService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 在线用户 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService
{
    /**
     * 通过登录地址查询信息
     * 
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     * 
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user)
    {
        if (StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     * 
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     * 
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser user)
    {
        if (StringUtils.isNull(user) && StringUtils.isNull(user.getUser()))
        {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setUserId(String.valueOf(user.getUserId()));
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setNickName(user.getNikename());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(user.getLoginTime());
        if (StringUtils.isNotNull(user.getUser().getDept()))
        {
            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
        }
        if (StringUtils.isNotNull(user.getUser().getCom()))
        {
            sysUserOnline.setCompanyName(user.getUser().getCom().getCompanyName());
        }
        return sysUserOnline;
    }


    @Override
    public List<SysUserOnline> getOnlineUser(String userId, PageDomain page) {
        if (StringUtils.isEmpty(userId)) {
            userId = "";
        }
        // 创建集合
        List<SysUserOnline> arrayList = new ArrayList<>();
        int start = PageUtil.getStart(page.getPageNum() - 1, page.getPageSize());
        // 分页查询数据
        List<String> sessionIdList = StpUtil.searchSessionId(userId, start, page.getPageSize(), false);
        for (String sessionId : sessionIdList) {
            SaSession session = StpUtil.getSessionBySessionId(sessionId);
            for (TokenSign ts : session.getTokenSignList()) {
                LoginUser loginUser = SecurityUtils.getLoginUser(ts.getValue());
                SysUserOnline userOnline = loginUserToUserOnline(loginUser);
                userOnline.setDevice(ts.getDevice());
                arrayList.add(userOnline);
            }
        }
        return arrayList;
    }
}
