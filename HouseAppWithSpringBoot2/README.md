本项目从[HouseApp](https://github.com/huawei-microservice-demo/HouseApp)演变而来，实现一样的业务场景，但是基于不一样的技术架构。

* 开发框架采用Spring Boot 2 + CSE
* 事务框架采用Saga，该框架提供了TCC、Saga两种事务模式的支持

# 安装和运行
## 准备mysql数据库
1. 安装mysql数据库
2. 创建数据库： creata database saga;

## 编译运行Alpha
1. 下载最新Saga代码
2. 修改alpha\alpha-server\src\main\resources\application.yaml文件.   
配置正确的mysql信息:   
```
spring:
  profiles: mysql
  datasource:
    username: root
    password: root
    url: jdbc:mysql://localhost:3306/saga?useSSL=false
    platform: mysql
    continue-on-error: true
```

修改服务器监听的端口（可选）

```
server:
  port: 6090

alpha:
  server:
    host: 0.0.0.0
    port: 7080
```

3. 编译项目：mvn clean install -Pmysql -DskipTests
4. 启动Alpha

```
cd alpha\alpha-server\target\saga
java -Dspring.profiles.active=mysql -D"spring.datasource.url=jdbc:mysql://localhost:3306/saga?useSSL=false" -Dspring.datasource.username=root -Dspring.datasource.password=root -jar alpha-server-0.3.0-SNAPSHOT-exec.jar
java -Dspring.profiles.active=mysql -jar alpha-server-0.3.0-SNAPSHOT-exec.jar
```

5. 检查。服务正常启动后，可以进入数据库saga. 可以看到自动生成了一些表。

```
mysql> show tables;
+-----------------------+
| Tables_in_saga        |
+-----------------------+
| command               |
| tcc_global_tx_event   |
| tcc_participate_event |
| tcc_tx_event          |
| txevent               |
| txtimeout             |
+-----------------------+
```

## 编译运行HouseAPP
1. 下载最新代码
2. 编译：mvn clean install
3. 运行买房系统： start.bat。会启动6个微服务，如果单机运行可能需要占用大量内存，启动会比较慢，建议准备多个机器执行，同时修改alpha的地址信息。
4. 检验：输入http://localhost:18080/, 用户user2，密码test登录。目前可以进行重置数据和抢购操作。
