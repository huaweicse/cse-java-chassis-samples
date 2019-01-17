package com.huawei.cse.houseapp.customer.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.huawei.cse.houseapp.account.api.AccountEndpoint;
import com.huawei.cse.houseapp.customer.api.CustomerEndpoint;
import com.huawei.cse.houseapp.product.api.ProductEndpoint;
import com.huawei.cse.houseapp.product.api.ProductInfo;
import com.huawei.cse.houseapp.user.api.UserEndpoint;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestSchema(schemaId = "customer")
@RequestMapping(path = "/")
public class CustomerEndpointImpl implements CustomerEndpoint {
  @Inject
  public CustomerService customerSerivce;

  @RpcReference(microserviceName = "user-service", schemaId = "user")
  private UserEndpoint userService;

  @RpcReference(microserviceName = "product-service", schemaId = "product")
  private ProductEndpoint productService;

  @RpcReference(microserviceName = "account-service", schemaId = "account")
  private AccountEndpoint accountService;

  @Override
  @PostMapping(path = "buyWithTransactionSaga")
  @ApiResponse(code = 400, response = String.class, message = "buy failed")
  public boolean buyWithTransactionSaga(@RequestHeader(name = "userId") long userId,
      @RequestParam(name = "productId") long productId, @RequestParam(name = "price") double price) {
    return customerSerivce.buyWithTransactionSaga(userId, productId, price);
  }

  @Override
  @PostMapping(path = "buyWithTransactionTCC")
  public boolean buyWithTransactionTCC(@RequestHeader(name = "userId") long userId,
      @RequestParam(name = "productId") long productId, @RequestParam(name = "price") double price) {
    return customerSerivce.buyWithTransactionTCC(userId, productId, price);
  }

  @Override
  @PostMapping(path = "buyWithoutTransaction")
  @ApiResponse(code = 400, response = String.class, message = "buy failed")
  public boolean buyWithoutTransaction(@RequestHeader(name = "userId") long userId,
      @RequestParam(name = "productId") long productId, @RequestParam(name = "price") double price) {
    // product will lock, put it in front
    if (!productService.buyWithoutTransaction(productId, userId, price)) {
      throw new InvocationException(400, "product already sold", "product already sold");
    }
    if (!userService.buyWithoutTransaction(userId, price)) {
      throw new InvocationException(400, "user do not got so much money", "user do not got so much money");
    }
    if (!accountService.payWithoutTransaction(userId, price)) {
      throw new InvocationException(400, "pay failed", "pay failed");
    }
    return true;
  }

  @ApiOperation(hidden = true, value = "")
  public void cancelBuy(long userId, long productId, double price) {
    //不做事情。生产代码可以记录审计日志。
  }

  @ApiOperation(hidden = true, value = "")
  public void confirmBuy(long userId, long productId, double price) {
    //不做事情。生产代码可以记录审计日志。 
  }

  //实际是重置数据接口，不改名字了。 
  @Override
  @PostMapping(path = "login")
  public long login(@RequestParam(name = "username") String username,
      @RequestParam(name = "password") String password) {
    productService.login(username, password);
    accountService.login(username, password);
    return userService.login(username, password);
  }

  @Override
  @GetMapping(path = "searchAllProducts")
  public List<ProductInfo> searchAllProducts() {
    return productService.searchAllForCustomer();
  }

  @Override
  @GetMapping(path = "balance")
  public String balance() {
    double user = userService.queryReduced();
    double acct = accountService.queryReduced();
    double prod = productService.queryReduced();
    return String.format("user:%s;acct:%s;prod:%s", user, acct, prod);
  }
}
