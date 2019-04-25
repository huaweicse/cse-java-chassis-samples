# 编译运行

## 准备mysql数据库
1. 安装mysql数据库
2. 创建数据库： creata database saga;
3. 假设用户名密码为root/root，注意修改


## 启动saga(For saga 0.4.0 +)
1. 下载saga 0.4.0. 创建目录./plugins ，并将mysql驱动mysql-connector-java-8.0.12.jar拷贝到该目录

2. 执行命令

```
java -Dspring.profiles.active=mysql -Dloader.path=./plugins -Dalpha.server.host=0.0.0.0 -Dalpha.server.port=7080 -Dserver.port=6090 -Dalpha.cluster.address=localhost:7080 -D"spring.datasource.url=jdbc:mysql://localhost:3306/saga?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8" -Dspring.datasource.username=root -Dspring.datasource.password=root -jar alpha-server-0.4.0-exec.jar
```

3. 检查。服务正常启动后，可以进入数据库saga. 可以看到自动生成了一些表。

```
mysql> show tables;
+-----------------------+
| Tables_in_saga        |
+-----------------------+
| command               |
| master_lock           |
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
