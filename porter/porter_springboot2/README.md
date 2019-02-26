这个项目帮助开发者学习如何使用CSE开发完整的微服务。 对应的指导文档参考：https://huaweicse.github.io/cse-java-chassis-doc/featured-topics/develop-microservice-using-cse.html
这个项目实现的功能非常简单，用户登录后，上传一个文件和删除一个文件，验证了没有权限的用户无法删除文件。

# 编译和运行

* 编译

```
mvn clean install
```

* 运行
  * 安装mysql数据库，设置用户名密码（假设为root/root）
  * 执行脚本create_db_user.sql

* 设置环境变量
```
export JAVA_OPT="-Dcse.credentials.accessKey=YourAccessKey -Dcse.credentials.secretKey=YourSecretKey -Dfile.encoding=UTF-8"
```

* 启动user-service:

```
java $JAVA_OPT -Dspring.datasource.url="jdbc:mysql://localhost/porter_user_db?useSSL=false" -Dspring.datasource.username=root -Dspring.datasource.password=root -jar porter-user-service-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
```

* 启动file-service:

```
java $JAVA_OPT -jar porter-file-service-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
```

* 启动gateway-serivce:

gateway-service包含了静态页面文件，在resources/ui目录。首先需要将页面文件拷贝到WEB主目录（相对路径，当前运行目录），比如: webapp，然后将ui目录整体拷贝到webapp/ui目录。启动：
```
java $JAVA_OPT -Dgateway.webroot=webapp -jar porter-gateway-service-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
```

# 使用

1. 输入: http://localhost:9090/ui/login.html 使用admin或者guest登陆，密码为test。
2. 选择一个文件上传，上传成功，上传成功后的文件会保存在file-service的当前目录， 文件名称是一个随机的数字，这个数字就是文件ID。
3. 删除文件：输入上一步的文件ID，点击删除。 如果是admin用户，上传成功；如果是guest用户，上传失败。


