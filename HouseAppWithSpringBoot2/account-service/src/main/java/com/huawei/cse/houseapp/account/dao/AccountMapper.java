package com.huawei.cse.houseapp.account.dao;

public interface AccountMapper {
    AccountInfo getAccountInfo(long userId);
    
    void updateAccountInfo(AccountInfo info);
    
    void createAccountInfo(AccountInfo info);
    
    void clear();

    Double queryReduced();
}
