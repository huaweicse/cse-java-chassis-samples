## 服务消费者接入CSE

### 镜像构建
a. 已提供Dockerfile和start.sh，可以直接在华为云微服务云平台[创建构建Job](https://servicestage.huaweicloud.com/servicestage/?project=cn-north-1#/pipeline/createjob)和[流水线](https://servicestage.huaweicloud.com/servicestage/?project=cn-north-1#/pipeline/create?from=pipeline.list)

b. 华为云微服务平台编译时默认使用自带的maven仓库，若使用自定义的maven仓库，可参考以下两种方法： 1、请将您的settings.xml存放到该项目的代码库根目录下； 2、在该项目的pom文件中设置用户repositories配置。
