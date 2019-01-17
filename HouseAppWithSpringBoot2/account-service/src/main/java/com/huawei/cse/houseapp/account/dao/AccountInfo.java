package com.huawei.cse.houseapp.account.dao;

public class AccountInfo {
    private long userId;
    private double totalBalance;
    private boolean reserved;
    
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public double getTotalBalance() {
        return totalBalance;
    }
    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }
    public boolean isReserved() {
        return reserved;
    }
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }
}
