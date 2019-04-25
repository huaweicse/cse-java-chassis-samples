package com.huawei.cse;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId = "StoreEndpoint")
@RequestMapping(path = "/")
public class StoreEndpoint {  
  @RpcReference(microserviceName = "authStub", schemaId="authStub")
  private AuthService authService;
  
  @GetMapping("/product/{id}")
  public String getProduct(@PathVariable String id) {
    return "product id : " + id;
  }

  @GetMapping("/order/{id}")
  public String getOrder(@PathVariable String id) {
    return "order id : " + id;
  }

  @GetMapping("/auth")
  public Token auth() {
    return authService.auth("user_1", "123456", "password", "read", "client_2", "123456");
  }
}
