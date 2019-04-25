package com.huawei.cse.houseapp.user.dao;

import com.huawei.cse.houseapp.user.api.UserInfo;

public interface UserMapper {
    void createUser(UserInfo userInfo);

    UserInfo getUserInfo(long userId);

    void updateUserInfo(UserInfo info);
    
    void clear();

    Double queryReduced();
}
