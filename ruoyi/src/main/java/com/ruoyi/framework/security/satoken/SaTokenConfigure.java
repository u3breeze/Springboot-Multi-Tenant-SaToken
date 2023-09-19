package com.ruoyi.framework.security.satoken;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.SaJwtTemplate;
import cn.dev33.satoken.jwt.SaJwtUtil;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token整合jwt(Simple简单模式)
 * https://sa-token.cc/doc.html#/plugin/jwt-extend?id=_5%e3%80%81%e4%b8%8d%e5%90%8c%e6%a8%a1%e5%bc%8f%e7%ad%96%e7%95%a5%e5%af%b9%e6%af%94
 * @author yh_liu
 * @version v1.0
 * @since 2022-11-28
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {


    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

//    /**
//     * 重写 Sa-Token 框架内部算法策略
//     */
//    @Autowired
//    public void setSaJwtTemplate() {
//        SaJwtUtil.setSaJwtTemplate(new SaJwtTemplate() {
//            @Override
//            public String generateToken(JWT jwt, String key) {
//                System.out.println("------ 这里可以自定义 token 生成-----------");
//                return super.generateToken(jwt, key);
//            }
//        });
//    }

    /**
     * 注册拦截器
     *
     * @param registry 仓储
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 的路由拦截器
        registry.addInterceptor(new SaInterceptor(handle -> {

            SaRouter
                    .match("/**")
                    .notMatch(excludePaths())
                    .check(r -> StpUtil.checkLogin());

        })).addPathPatterns("/**");
    }

    /**
     * 动态获取哪些path可以忽略鉴权
     */
    public List<String> excludePaths() {
        // 此处仅为示例，实际项目你可以写任意代码来查询这些path,也可根据@SaIgnore忽略请求
        List<String> paths = new ArrayList<>();
        paths.add("/*.html");
        paths.add("/**/*.html");
        paths.add("/**/*.js");
        paths.add("/**/*.css");
        paths.add("/swagger-resources/**");
        paths.add("/webjars/*");
        paths.add("/*/api-docs");
        return paths;
    }


}
