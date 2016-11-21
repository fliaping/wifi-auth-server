# 简介

本项目是以 wifidog 为认证网关的认证服务器, 开发语言为 Java 1.8, 使用了 spring-boot框架, 采用 restful 风格的设计。
有关 wifidog 的协议及用法,请参考 [利用Wifidog实现微信wifi连接](https://blog.fliaping.com/the-implements-of-weixin-wifi-by-using-wifidog-gateway/)


## 认证方式

认证方式目前只支持微信认证,正在增加其它认证方式

## 服务API

使用了[swagger-ui](http://swagger.io/swagger-ui/) 来提供在线的API说明及测试,详细请参考用法部分。

## 后台管理

由于开放了 restful 风格的 api, 可自己定制前端。

PS: 目前正在用 angular2 实现一个 admin panel,敬请期待。。。

# 用法

## 直接导入 IntelliJ IDEA

## 用 gradle 运行

* `gradle bootRun`
