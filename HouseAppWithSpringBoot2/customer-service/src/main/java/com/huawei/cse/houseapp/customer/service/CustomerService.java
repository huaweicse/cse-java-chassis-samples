package com.huawei.cse.houseapp.customer.service;

import javax.ws.rs.core.Response.Status;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.saga.omega.context.annotations.SagaStart;
import org.apache.servicecomb.saga.omega.context.annotations.TccStart;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.stereotype.Service;

import com.huawei.cse.houseapp.account.api.AccountEndpoint;
import com.huawei.cse.houseapp.product.api.ProductEndpoint;
import com.huawei.cse.houseapp.user.api.UserEndpoint;

@Service
public class CustomerService {
  @RpcReference(microserviceName = "user-service", schemaId = "user")
  private UserEndpoint userService;

  @RpcReference(microserviceName = "product-service", schemaId = "product")
  private ProductEndpoint productService;

  @RpcReference(microserviceName = "account-service", schemaId = "account")
  private AccountEndpoint accountService;

  @SagaStart
  public boolean buyWithTransactionSaga(long userId,
      long productId, double price) {
    if (!userService.buyWithTransactionSaga(userId, price)) {
      throw new InvocationException(400, "user do not got so much money", "user do not got so much money");
    }
    if (!productService.buyWithTransactionSaga(productId, userId, price)) {
      throw new InvocationException(400, "product already sold", "product already sold");
    }
    if (!accountService.payWithTransactionSaga(userId, price)) {
      throw new InvocationException(400, "pay failed", "pay failed");
    }
    return true;
  }

  @TccStart
  public boolean buyWithTransactionTCC(long userId,
      long productId, double price) {
    if (!userService.buyWithTransactionTCC(userId, price)) {
      throw new InvocationException(Status.BAD_REQUEST, "user do not got so much money");
    }
    if (!productService.buyWithTransactionTCC(productId, userId, price)) {
      throw new InvocationException(Status.BAD_REQUEST, "product already sold");
    }
    if (!accountService.payWithTransactionTCC(userId, price)) {
      throw new InvocationException(Status.BAD_REQUEST, "pay failed");
    }
    return true;
  }
}
