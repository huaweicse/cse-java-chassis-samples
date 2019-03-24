这个项目主要演示如何使用spring security oauth2 + jwt实现认证。核心使用的组件包括：

```
<groupId>org.springframework.security.oauth</groupId>
<artifactId>spring-security-oauth2</artifactId>
```

和

```
<groupId>org.springframework.security</groupId>
<artifactId>spring-security-jwt</artifactId>
```

***重要：*** 本项目主要用于演示认证流程，系统的安全性取决于很多方面，包括用户密码的保护、JWT Token的生成和保护，以及其他过程。为了简单，本示例均没有提供这方面的考虑，因此不要将示例代码直接用于生成代码。

## 各个微服务开发技术说明

由于这个示例项目混合了Spring MVC， CSE REST和注册发现等多种开发技术， 开发者需要首先理解这些技术的差异，参考[基于CSE的微服务架构实践-Spring Boot技术栈选型](https://bbs.huaweicloud.com/blogs/eca98a6f399a11e9bd5a7ca23e93a891) 。采用不同技术的服务，在服务治理（体现为运行时）方面支持的能力是不同的。 

本文的使用Spring Security OAuth2的部分示例代码参考了[oauth2_jwt](https://github.com/simondongji/SpringCloudProject/tree/master/oauth2_jwt)，作者的说明[Spring Security OAuth2实现使用JWT](https://blog.csdn.net/AaronSimon/article/details/84071811)	
, 还参考了[从零开始的Spring Security Oauth2](http://blog.didispace.com/spring-security-oauth2-xjf-1/)。

* 认证服务器 springboot2-oauth2-server
由于CSE并没有提供oauth2的实现，因此这个项目主要基于Spring Boot MVC + spring security oauth2实现，里面的运行时是Spring Boot MVC， 不执行CSE的handler处理链。但是本项目仍然集成了CSE的功能，将这个服务注册到服务中心，以供网关等做服务发现使用。下面的配置项让所有的请求都走Spring Boot MVC运行时：

```
## using spring mvc rest server to handle requests
server.servlet.path: /
servicecomb.rest.servlet.urlPattern: /cse/*
```

* 资源服务器 springboot2-oauth2-resource-server
这个项目提供了资源服务器的一个鉴权示例。也采用Spring Boot MVC + spring security oauth2实现, 里面的运行时是Spring Boot MVC， 不执行CSE的handler处理链。本项目也集成了CSE的功能，将这个服务注册到服务中心，以供网关等做服务发现使用。
。下面的配置项让所有的请求都走Spring Boot MVC运行时：

```
## using spring mvc rest server to handle requests
server.servlet.path: /
servicecomb.rest.servlet.urlPattern: /cse/*
```

* 资源服务器 cse-oauth2-resource-server
这个项目提供了资源服务器的一个鉴权示例。采用CSE REST + spring security oauth2实现，里面的运行时是CSE，执行CSE的handler处理链。由于Spring Security只能够在同步模式使用，需要关闭CSE的业务线程池，与Tomcat的线程池合并
```
cse:
  executors:
    default: servicecomb.executor.reactive
```

* 网关 gateway-service
网关只集成了服务发现，不走CSE的handler处理链。 服务发现提供基本的[负载均衡、实例隔离](https://docs.servicecomb.io/java-chassis/zh_CN/references-handlers/loadbalance.html)等功能。 网关也不做认证、鉴权操作。在oauth2情况下，认证鉴权都在资源服务器进行。

## 如何测试和使用
首先启动这几个微服务

请求token:
```
http://localhost:8888/springboot2-oauth2-server/oauth/token?username=user_1&password=123456&grant_type=password&scope=read&client_id=client_2&client_secret=123456
```

请求资源：
拒绝：
```
http://localhost:8888/cse-oauth2-resource-server/order/1?access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib3JkZXIiXSwiZXhwIjoxNTUzNDg0ODgxLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiMTZlYWJhZDktNTcxZS00OWRjLWE4Y2UtNzI1OGZlYjk4MDNiIiwiY2xpZW50X2lkIjoiY2xpZW50XzIiLCJzY29wZSI6WyJyZWFkIl19.ZDVdHLv-QIyBlQWWwrngJ1H8DleEOMP04qgEoid_ha8
```
允许：
```
http://localhost:8888/cse-oauth2-resource-server/order/order2/1?access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib3JkZXIiXSwiZXhwIjoxNTUzNDc0OTk2LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiZGVhY2Y3YWMtOTgxYi00ZDdjLTljOWMtOTRjZDA4ZDY1NzQxIiwiY2xpZW50X2lkIjoiY2xpZW50XzIiLCJzY29wZSI6WyJyZWFkIl19.eSGGlfpmN76BgxhsBxak29PDPXy1l8SyH6g7exszIMY
```

