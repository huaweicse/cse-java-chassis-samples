# 使用华为云DevCloud平台开发、构建、部署
[华为云](http://www.huaweicloud.com)[DevCloud](https://devcloud.huaweicloud.com)平台提供项目管理、代码托管、代码检查、编译构建、部署以及流水线的功能，使用DevCloud以及流水线，可以方便地开展开发与部署工作。

[点击此链接](https://devcloud.huaweicloud.com/m/openApp.html?projectUUID=7fc4de2efc294aefa1ecd78b0295d5d9&page=joinProject)加入到本项目中。

## 代码托管
DevCloud提供了企业级的代码托管服务（gitlab），相比于github的优势是私密性，开发者可以创建自己私有的代码仓库，费用比github等厂商要便宜很多。

由于本项目原始代码托管在github上（开源考虑），DevCloud托管的代码库不能直接从github fork，因此需要手动trace DevCloud->github 两个仓库的同步。下面是操作方法。

### 手动 trace DevCloud->github
#### 1. clone DevCloud 的代码库到本地
```
$ git clone git@codehub.devcloud.huaweicloud.com:0da54311cad54affa2f3a4c70df39de4/HouseApp.git
Cloning into 'HouseApp'...
The authenticity of host 'codehub.devcloud.huaweicloud.com (117.78.39.149)' can't be established.
RSA key fingerprint is SHA256:u1essdo63WtE5PbFDwEYzrZCHpgR/pHtCoFzFdqPeKE.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added 'codehub.devcloud.huaweicloud.com,117.78.39.149' (RSA) to the list of known hosts.
remote: Counting objects: 6, done.
remote: Compressing objects: 100% (4/4), done.
remote: Total 6 (delta 1), reused 0 (delta 0)
Receiving objects: 100% (6/6), 4.29 KiB | 313.00 KiB/s, done.
Resolving deltas: 100% (1/1), done.
```
#### 2. 创建一个本地分支 github-master ，稍后用此分支 trace 到 github 上的仓库
```
xx@yy MINGW64 /e/workspace/devcloud/HouseApp (master)
$ git checkout -b github-master
Switched to a new branch 'github-master'

xx@yy MINGW64 /e/workspace/devcloud/HouseApp (github-master)
$ git branch --list
* github-master
  master
```
#### 3. trace 本地分支 github-master 到 github 的 master 分支
1. 添加远程仓库
```
xx@yy MINGW64 /e/workspace/devcloud/HouseApp (github-master)
$ git remote add github https://github.com/ibusybox/HouseApp.git

xx@yy MINGW64 /e/workspace/devcloud/HouseApp (github-master)
$ git remote -v
github  https://github.com/ibusybox/HouseApp.git (fetch)
github  https://github.com/ibusybox/HouseApp.git (push)
origin  git@codehub.devcloud.huaweicloud.com:0da54311cad54affa2f3a4c70df39de4/HouseApp.git (fetch)
origin  git@codehub.devcloud.huaweicloud.com:0da54311cad54affa2f3a4c70df39de4/HouseApp.git (push)
```

2. fetch
```
xx@yy MINGW64 /e/workspace/devcloud/HouseApp (github-master)
$ git fetch github master
From https://github.com/ibusybox/HouseApp
 * branch            master     -> FETCH_HEAD
 * [new branch]      master     -> github/master
```
3. trace 本地分支与远程仓库
```
xx@yy MINGW64 /e/workspace/devcloud/HouseApp (github-master)
$ git branch --set-upstream-to=github/master
Branch github-master set up to track remote branch master from github.
```
4. confirm upstream
```
xx@yy MINGW64 /e/workspace/devcloud/HouseApp (github-master)
$ git rev-parse --abbrev-ref --symbolic-full-name @{u}
github/master
```
5. pull 远程仓库到本地分支（如果有冲突则手动修复）
```
xx@yy MINGW64 /e/workspace/devcloud/HouseApp (github-master)
$ git pull --allow-unrelated-histories github master
From https://github.com/ibusybox/HouseApp
 * branch            master     -> FETCH_HEAD
Auto-merging README.md
CONFLICT (add/add): Merge conflict in README.md
Automatic merge failed; fix conflicts and then commit the result.
```

6. 切换回master分支
```
xx@yy MINGW64 /e/workspace/devcloud/HouseApp (github-master)
$ git checkout master
```

7. merge github-master 分支到 master 分支（如果有冲突则手动修复）
```
xx@yy MINGW64 /e/workspace/devcloud/HouseApp (master)
$ git merge --allow-unrelated-histories github/master
Auto-merging README.md
CONFLICT (add/add): Merge conflict in README.md
Automatic merge failed; fix conflicts and then commit the result.
```

8. 提交 master 分支到远程仓库
```
xx@yy MINGW64 /e/workspace/devcloud/HouseApp (master|MERGING)
$ git add -A

xx@yy MINGW64 /e/workspace/devcloud/HouseApp (master|MERGING)
$ git commit

xx@yy MINGW64 /e/workspace/devcloud/HouseApp (master)
$ git push origin master
Counting objects: 449, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (243/243), done.
Writing objects: 100% (449/449), 3.08 MiB | 1.18 MiB/s, done.
Total 449 (delta 115), reused 445 (delta 114)
To codehub.devcloud.huaweicloud.com:0da54311cad54affa2f3a4c70df39de4/HouseApp.git
   8a0a07f..dfa40f1  master -> master
```

9. 代码开发使用github作为源，在github提交后，通过上述1~8步骤，同步到DevCloud上的代码仓库。

## 编译构建
### maven 编译
在 DevCloud 上创建构建任务，构建任务中指定 ```-s settings.xml``` 作为maven参数；归档文件需要指定为 ```${service_name}/target/*.jar```

### 构建 docker 镜像
在 DevCloud 上创建构建任务，在 maven install 之后，增加构建 docker image 步骤

## 部署

## 流水线