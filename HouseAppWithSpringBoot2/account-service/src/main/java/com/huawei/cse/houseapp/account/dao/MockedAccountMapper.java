package com.huawei.cse.houseapp.account.dao;

import java.util.HashMap;
import java.util.Map;

public class MockedAccountMapper implements AccountMapper {
    Map<Long, AccountInfo> accounts = new HashMap<>();

    public MockedAccountMapper() {
    }

    @Override
    public AccountInfo getAccountInfo(long userId) {
        return accounts.get(userId);
    }

    @Override
    public void updateAccountInfo(AccountInfo info) {

    }

    @Override
    public void createAccountInfo(AccountInfo info) {
       accounts.putIfAbsent(info.getUserId(), info);
    }

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public Double queryReduced() {
        double remain = 0;
        for(AccountInfo i : accounts.values()) {
            remain = remain + i.getTotalBalance();
        }
        return accounts.size() * 8000000D - remain;
    }

}
