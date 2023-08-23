# Multi-Tenant-SaToken

[简体中文](https://gitee.com/willf/springboot-multi-tenant-sa-token/blob/master/README_zh.md)

#### Introduction
Multi-Tenant-SaToken is an upgraded version of [RuoYi-Vue-Multi-Tenant](https://gitee.com/leslie8195/ruo-yi-vue-multi-tenant) （SpringBoot，sa-token，mybatis-plus，Vue & Element），It is a simple, elegant, and lightweight framework for a multi-tenant system.

[github地址](https://github.com/u3breeze/Springboot-Multi-Tenant-SaToken)

#### Upgrades and optimizations
1. Refactored the authentication module for permissions, replacing Spring Security with [sa-token](https://sa-token.cc) to simplify and streamline authentication.
2. Integrated jwt with Sa-Token([Simple mode](https://sa-token.cc/doc.html#/plugin/jwt-extend)) 
3. Upgraded Spring Boot to 2.6.8.
4. Upgraded MyBatis to MyBatis-Plus.
5. Upgraded Swagger and used Knife4j to enhance document generation.
6. Optimized the handling of permissions for super administrators and tenant administrators, uniformly managed by sa-token without the need for manual if statements.
7. Upgraded dependency package versions.
8. User accounts are isolated by tenants, and different tenants can use the same account.

#### Environment and deployment
The environment and deployment can be viewed in[RuoYi-Vue-Multi-Tenant](https://gitee.com/leslie8195/ruo-yi-vue-multi-tenant) ，and the process is the same.

#### Acknowledgements
1. [sa-token](https://sa-token.cc) Lightweight Java authentication framework for permissions.
2. [RuoYi-Vue-Multi-Tenant](https://gitee.com/leslie8195/ruo-yi-vue-multi-tenant) Multi-tenant framework based on RuoYi-Vue.
