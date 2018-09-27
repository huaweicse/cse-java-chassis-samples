package com.huawei.cse.porter.user.dao;

public interface UserMapper {
    void createUser(UserInfo userInfo);

    UserInfo getUserInfo(String userName);
}
