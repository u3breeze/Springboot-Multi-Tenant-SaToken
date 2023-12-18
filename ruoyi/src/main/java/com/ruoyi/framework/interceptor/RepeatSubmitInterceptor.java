package com.ruoyi.framework.interceptor;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.interceptor.annotation.RepeatSubmit;
import com.ruoyi.framework.web.domain.AjaxResult;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 防止重复提交拦截器
 * 
 * @author ruoyi
 */
@Component
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor
{
    /**
     * 防止重复提交拦截器
     * @param request http请求
     * @param response http响应
     * @param handler 处理器
     * @return 是否通过
     */
    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable  HttpServletResponse response, @Nullable Object handler)
    {
        if (handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null && (this.isRepeatSubmit(request) && response != null))
            {
                AjaxResult ajaxResult = AjaxResult.error("不允许重复提交，请稍后再试");
                ServletUtils.renderString(response, JSON.toJSONString(ajaxResult));
                return false;
            }
        }
        return true;
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param  request http请求
     * @return 是否重复提交
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request);
}