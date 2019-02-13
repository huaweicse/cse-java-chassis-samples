这个项目帮助开发者学习如何使用CSE开发完整的微服务。 对应的指导文档参考：https://huaweicse.github.io/cse-java-chassis-doc/featured-topics/develop-microservice-using-cse.html

# 编译和运行

* 编译

```
mvn clean install
```

* 运行
  * 安装mysql数据库，设置用户名密码（假设为root/root）
  * 执行脚本create_db_user.sql
  * 启动user-service:

```
java -Ddb.url="jdbc:mysql://localhost/porter_user_db?useSSL=false" -Ddb.username=root -Ddb.password=root -jar porter-user-service-0.0.1-SNAPSHOT.jar
```

  * 启动file-service:

```
java -jar porter-file-service-0.0.1-SNAPSHOT.jar
```

  * 启动gateway-serivce:
  
```
java -jar porter-gateway-service-0.0.1-SNAPSHOT.jar
```
