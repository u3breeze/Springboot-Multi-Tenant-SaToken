package com.ruoyi.framework.security.service;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.BaseException;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.security.LoginBody;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysCompany;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysCompanyService;
import com.ruoyi.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysAuthService
{
    private static final Logger log = LoggerFactory.getLogger(SysAuthService.class);
    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysCompanyService companyService;


    /**
     * 登录验证
     * @return 结果
     */
    public String login(LoginBody loginBody)
    {
        String comcode = loginBody.getComcode();
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        String code = loginBody.getCode();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + loginBody.getUuid();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户验证
        LoginUser loginUser;
        try
        {
            loginUser = loadUserByUsername(comcode, username, password);
        }
        catch (UserPasswordNotMatchException e)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        } catch (Exception e)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
            throw new CustomException(e.getMessage());
        }

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        loginUser.setDevice(loginBody.getDevice());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    private LoginUser loadUserByUsername(String comcode, String username, String password) throws UserPasswordNotMatchException
    {
        SysCompany sysCompany = companyService.selectSysCompanyByCode(comcode);
        if (ObjectUtil.isNull(sysCompany) || CharSequenceUtil.isBlank(sysCompany.getId()))
        {
            log.info("租户Code：{} 不存在.", comcode);
            throw new UserPasswordNotMatchException();
        }
        SysUser user = userService.selectUserByUserName(sysCompany.getId(), username);
        if (StringUtils.isNull(user)
                || !SecurityUtils.matchesPassword(password, user.getPassword()))
        {
            log.info("登录用户：{} 不存在，或密码错误.", username);
            throw new UserPasswordNotMatchException();
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }

        return new LoginUser(user);
    }

    /**
     * 登出
     */
    public void logout() {
        // 记录用户退出日志
        LoginUser loginUser = tokenService.getLoginUser();
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginUser.getUsername(), Constants.LOGOUT, "退出成功"));

        tokenService.logoutUser();
    }



}
