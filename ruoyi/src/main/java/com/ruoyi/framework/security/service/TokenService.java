package com.ruoyi.framework.security.service;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.security.LoginUser;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Component;


/**
 * token验证处理
 * 
 * @author will
 */
@Component
public class TokenService
{
    /**
     * 获取用户身份信息
     * 
     * @return 用户信息
     */
    public LoginUser getLoginUser()
    {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser)
    {
        StpUtil.setTokenValue(StpUtil.getTokenValue(), SaLoginConfig.setExtra(SecurityUtils.AUTH_EXTRA_KEY_LOGIN_USER, loginUser));
    }

    /**
     * 用户登出
     */
    public void logoutUser()
    {
        StpUtil.logout();
    }

    /**
     * 创建令牌
     * 
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser)
    {
        setUserAgent(loginUser);
        // 所有验证通过后开始登录
        StpUtil.login(loginUser.getUserId(), SaLoginConfig.setExtra(SecurityUtils.AUTH_EXTRA_KEY_LOGIN_USER, loginUser).setDevice(loginUser.getDevice()));
        // 登录后获取token信息
        return StpUtil.getTokenValue();
    }

    /**
     * 设置用户代理信息
     * 
     * @param loginUser 登录信息
     */
    private void setUserAgent(LoginUser loginUser)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
        loginUser.setLoginTime(System.currentTimeMillis());
    }

}
