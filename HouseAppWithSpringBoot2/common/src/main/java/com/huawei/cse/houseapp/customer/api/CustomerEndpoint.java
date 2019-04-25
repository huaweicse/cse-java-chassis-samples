package com.huawei.cse.houseapp.customer.api;

import java.util.List;

import com.huawei.cse.houseapp.product.api.ProductInfo;

public interface CustomerEndpoint {
  boolean buyWithTransactionSaga(long userId, long productId, double price);

  boolean buyWithTransactionTCC(long userId, long productId, double price);

  boolean buyWithoutTransaction(long userId, long productId, double price);

  long login(String user, String password);

  String balance();

  public List<ProductInfo> searchAllProducts();
}
