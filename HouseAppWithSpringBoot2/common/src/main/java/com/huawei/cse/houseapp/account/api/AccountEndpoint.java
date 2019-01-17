package com.huawei.cse.houseapp.account.api;

public interface AccountEndpoint {
  boolean payWithTransactionSaga(long userid, double amount);

  boolean payWithTransactionTCC(long userid, double amount);

  boolean payWithoutTransaction(long userid, double amount);

  long login(String userName, String password);

  double queryReduced();
}
