package com.huawei.cse.porter.user.endpoint;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestSchema(schemaId = "TimeoutEndpoint")
@RequestMapping(path = "/")
public class TimeoutEndpoint {
  @GetMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
  public String hello(@RequestParam("name") String name) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return name;
  }
}
