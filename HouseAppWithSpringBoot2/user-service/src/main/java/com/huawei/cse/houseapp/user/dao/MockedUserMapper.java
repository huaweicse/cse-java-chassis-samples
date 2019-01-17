package com.huawei.cse.houseapp.user.dao;

import java.util.HashMap;
import java.util.Map;

import com.huawei.cse.houseapp.user.api.UserInfo;

public class MockedUserMapper implements UserMapper {
    Map<Long, UserInfo> users = new HashMap<>();

    public MockedUserMapper() {
    }

    @Override
    public UserInfo getUserInfo(long userid) {
        return users.get(userid);
    }

    @Override
    public void updateUserInfo(UserInfo info) {
        // reference already changed
    }

    @Override
    public void createUser(UserInfo info) {
        users.putIfAbsent(info.getUserId(), info);
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public Double queryReduced() {
        double remain = 0;
        for(UserInfo i : users.values()) {
            remain = remain + i.getTotalBalance();
        }
        return users.size() * 10000000D - remain;
    }

}
