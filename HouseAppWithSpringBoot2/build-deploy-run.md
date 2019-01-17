# Build/Deploy/Run
## 本地开发环境上 Build/Deploy
### 软件下载及准备
```
cd /home
yum update
yum install -y git  wget
git clone https://github.com/JeffyLiu003/HouseApp.git
wget http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.5.3/binaries/apache-maven-3.5.3-bin.tar.gz
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u161-b12/2f38c3b165be4555a1fa6e98c45e0808/jdk-8u161-linux-x64.tar.gz
tar zxf apache-maven-3.5.3-bin.tar.gz
tar zxf jdk-8u161-linux-x64.tar.gz
export PATH=$PATH:/home/apache-maven-3.5.3/bin:/home/jdk1.8.0_161/bin
mvn -v 
java -version
```

### Build
#### 1. maven install
1. 克隆代码到本地
2. 到项目根目录，执行 ```maven install -f pom.xml -s settings.xml ``` 编译

#### 2. build docker images
1. 到项目的根目录
2. 执行 ```bash build-images.sh build <your tag>``` 本地构建镜像
3. 在 ServiceStage 镜像仓库创建 namespace/仓库 
4. 执行 docker login 登录到远程镜像中心
5. 执行 ```bash build-images.sh push <your tag> <your repo namespace> ``` 将本地镜像推送到远程仓库

### Deploy
#### 1. 创建RDS（MySQL），并创建业务数据库与表
1. 创建RDS(MySQL)，设置好用户密码，并记录下登录地址
2. MySQL配置如下
```
MySQL 版本：5.7
实例规格：4C16G
实例类型：单实例
```

#### 2. 创建DCS（Redis）
1. 创建DCS（Redis）
2. 记录下密码与登录地址
3. Redis配置如下
```
Redis版本：3.0.7
实例规格：4GB
实例类型：单实例
```

#### 3. 创建连接CSE注册中心所需凭证
微服务启动后CSE的SDK会自动连接CSE的注册中心，微服务接入到注册中心需要用户的AK/SK作为认证凭证。使用 ServiceStage 的 ConfigMap 对象存储此凭证。

用户基于 [cse-credentials.yaml](deployment/configmap-templates/cse-credentials.yaml) 模板文件，将自己实际的AK/SK填到文件中，然后在 ServiceStage 上 [创建 ConfigMap ](https://servicestage.huaweicloud.com/servicestage/#/stage/configs/newcreate/596aeb28-de26-11e7-a506-0255ac101e21/clusterName/default/configsName/create)

[cse-credentials.yaml](deployment/configmap-templates/cse-credentials.yaml) 模板文件内容如下：
```
kind: ConfigMap
apiVersion: v1
metadata:
  name: cse-credential
  namespace: default
data:
  certificate.yaml: |
    cse:
      credentials:
        accessKey: ak
        secretKey: sk
        akskCustomCipher: default  
```

**注意：**
1. 此 ConfigMap 中的 **certificate.yaml** key 对应的内容在部署时候，需要被mount为容器内的 /opt/CSE/etc/cipher 目录下。
2. 实际部署时候，需要替换 accessKey/secretKey 为实际的 AK/SK

#### 4. 创建访问RDS（MySQL）所需的凭证
访问MySQL的凭证保存在 ServiceStage 的 ConfigMap 中，通过环境变量的方式导出给应用使用。
本应用中， user-service/account-service/product-service 使用到了数据库 user_db/account_db/product_db ，需要创建三个 ConfigMap，三个 ConfigMap 的模板下载地址：
- [mysql-userdb.yaml](deployment/configmap-templates/mysql-userdb.yaml)
- [mysql-accountdb.yaml](deployment/configmap-templates/mysql-accountdb.yaml)
- [mysql-productdb.yaml](deployment/configmap-templates/mysql-productdb.yaml)

下面是 [mysql-userdb.yaml](deployment/configmap-templates/mysql-userdb.yaml) 模板的内容，**需要用户将data内的值修改为实际环境中对应的值**：

```
kind: ConfigMap
apiVersion: v1
metadata:
  name: mysql-accountdb
  namespace: default
data:
  db.host: "192.168.244.231"
  db.port: "8635"
  db.dbname: account_db
  db.username: root
  db.password: password
```

#### 5. 创建访问DCS（Redis）所需的凭证
访问Redis的凭证保存在 ServiceStage 的 ConfigMap 中，通过环境变量方式导出给应用使用。
本应用中， user-service/account-service/product-service/customer-service 使用到了Redis，用Redis做事务，四个微服务使用的是同一个Redis实例。

用户基于 [redis-credentials.yaml](deployment/configmap-templates/redis-credentials.yaml) 模板文件，将实际环境中的Redis（DCS）访问信息填到模板文件中。
[redis-credentials.yaml](deployment/configmap-templates/redis-credentials.yaml) 模板文件内容如下：

```
kind: ConfigMap
apiVersion: v1
metadata:
  name: redis-credential
  namespace: default
data:
  cse.tcc.transaction.redis.host: "192.168.244.231"
  cse.tcc.transaction.redis.port: "6379"
  cse.tcc.transaction.redis.password: redis-password
```
#### 5. 卷
cse-credential 这个 ConfigMap 是通过挂接卷的方式到容器中使用，挂接的卷的目录为: /opt/CSE/etc/cipher

#### 6. 环境变量
redis, mysql 访问凭证的 ConfigMap 是通过导出环境变量到容器中的方式使用的，各个环境变量与 ConfigMap 映射关系如下

1. APPLICATION_ID: 手动输入，对应 CSE 中的 application 
2. TCC_REDIS_HOST: configmap: redis-credential, key: cse.tcc.transaction.redis.host
3. TCC_REDIS_PORT: configmap: redis-credential, key: cse.tcc.transaction.redis.port
4. TCC_REDIS_PASSWD: configmap: redis-credential, key: cse.tcc.transaction.redis.password
5. DB_HOST: configmap: mysql-xxx_db, key: db.host
6. DB_PORT: configmap: mysql-xxx_db, key: db.port
7. DB_NAME: configmap: mysql-xxx_db, key: db.dbname
8. DB_USERNAME: configmap: mysql-xxx_db, key: db.username
9. DB_PASSWD: configmap: mysql-xxx_db, key: db.password

#### 7. 镜像版本
部署堆栈的时候，需要输入各个微服务正确的镜像版本

## 使用 DevCloud 流水线 Build/Deploy
[DevCloud](devcloud/)

## 访问应用
部署成功后，在 ServiceStage 的 应用上线 功能中找到 **edge-service** ，查看详情找到访问方式，追加URL访问：**http://eip:31080/ui/customer-website/login.html** 。

打开首页后，以 ```[user0,user99]``` 之间任何一个用户登录，密码为 ```test``` ，登录成功之后点 ```重置数据按钮``` ，成功后注销当前用户，再次登录，即可使用其他功能。
