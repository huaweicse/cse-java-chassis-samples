package com.huawei.cse.houseapp.product.api;

import java.util.List;

public interface ProductEndpoint {
  List<ProductInfo> searchAll(int userId);

  public List<ProductInfo> searchAllForCustomer();

  void addProduct(double price);

  public boolean buyWithTransactionSaga(long productId, long userId, double price);

  public boolean buyWithTransactionTCC(long productId, long userId, double price);
  
  public boolean buyWithoutTransaction(long productId, long userId, double price);

  long login(String userName, String password);

  double queryReduced();
}
