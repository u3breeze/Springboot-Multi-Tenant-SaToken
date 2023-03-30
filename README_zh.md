# Multi-Tenant-SaToken

[English](https://github.com/u3breeze/Springboot-Multi-Tenant-SaToken/blob/main/README.md)

#### 介绍
此项目 Multi-Tenant-SaToken 是基于[RuoYi-Vue-Multi-Tenant](https://gitee.com/leslie8195/ruo-yi-vue-multi-tenant) 的升级版（SpringBoot，sa-token，mybatis-plus，Vue & Element），是一个简约、优雅、轻量级的多租户系统的脚手架。

#### 升级和优化
1. 重构权限认证模块，替换spring security，使用 [sa-token](https://sa-token.cc) 让鉴权更简单、优雅。
2. Sa-Token整合jwt([Simple简单模式](https://sa-token.cc/doc.html#/plugin/jwt-extend)) 
3. springboot升级到2.6.8。
4. 升级mybatis为mybatis-plus。
5. 升级swagger，使用knife4j增强文档生成。
6. 优化超级管理员和租户管理员的权限处理，统一交给sa-token，无需要手动if判断。
7. 升级依赖包版本。

#### 环境和部署
环境和部署可查看[RuoYi-Vue-Multi-Tenant](https://gitee.com/leslie8195/ruo-yi-vue-multi-tenant) ，方式一样。

#### 感谢
1. [sa-token](https://sa-token.cc) 轻量级 java 权限认证框架
2. [RuoYi-Vue-Multi-Tenant](https://gitee.com/leslie8195/ruo-yi-vue-multi-tenant) 基于RuoYi-Vue扩展的多租户框架
