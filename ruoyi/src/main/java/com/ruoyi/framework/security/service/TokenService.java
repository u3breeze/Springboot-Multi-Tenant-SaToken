package com.ruoyi.framework.security.service;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.security.LoginUser;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Component;

import java.util.Map;


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
        SecurityUtils.setLoginUser(loginUser);
    }

    /**
     * 用户登出
     */
    public void logoutUser()
    {
        SecurityUtils.logoutUser();
    }

    /**
     * 创建令牌，在本服务中自动生成
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser)
    {
        return createToken(loginUser, null, null, null);
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @param token 令牌，支持从外部接入的token（如统一的sso server生成的token），如果不传，则在本服务中自动生成
     * @param tokenExpiresIn 外部接入令牌有效期
     * @param extraData 额外数据
     * @return 令牌
     */
    public String createToken(LoginUser loginUser, String token, Integer tokenExpiresIn, Map<String, Object> extraData)
    {
        setUserAgent(loginUser);
        // 设置到sa-token进行管理
        SaLoginModel loginModel = SaLoginConfig.setDevice(loginUser.getDevice()); // 设置设备标识
        if (CharSequenceUtil.isNotBlank(token)) { // 如果是外部接入的token，则设置token
            loginModel.setToken(token);
            loginModel.setTimeout(tokenExpiresIn);
        }
        if (ObjectUtil.isNotEmpty(extraData)) { // 如果有额外数据，则设置额外数据
            loginModel.setExtraData(extraData);
        }

        StpUtil.login(loginUser.getUserId(), loginModel);
        SecurityUtils.setLoginUser(loginUser); // 设置loginUser到sa-token的token session中
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
