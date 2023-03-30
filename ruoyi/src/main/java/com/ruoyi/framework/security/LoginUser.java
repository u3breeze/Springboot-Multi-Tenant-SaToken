package com.ruoyi.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.project.system.domain.SysUser;

import java.io.Serializable;

/**
 * 登录用户身份权限
 * 
 * @author will
 */
public class LoginUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 登陆时间
     */
    private Long loginTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录设备: pc, mobile etc.
     */
    private String device;

    /**
     * 用户信息
     */
    private SysUser user;


    public LoginUser()
    {
    }

    public LoginUser(SysUser user)
    {
        this.user = user;
    }

    @JsonIgnore
    public String getPassword()
    {
        return user.getPassword();
    }


    public String getUsername()
    {
        return user.getUserName();
    }

    public String getNikename()
    {
        return user.getNickName();
    }


    public Long getUserId()
    {
        return user.getUserId();
    }



    public Long getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Long loginTime)
    {
        this.loginTime = loginTime;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public SysUser getUser()
    {
        return user;
    }

    public void setUser(SysUser user)
    {
        this.user = user;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
