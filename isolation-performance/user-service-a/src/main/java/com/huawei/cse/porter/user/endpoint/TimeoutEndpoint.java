package com.huawei.cse.porter.user.endpoint;

import java.util.Random;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestSchema(schemaId = "TimeoutEndpoint")
@RequestMapping(path = "/")
public class TimeoutEndpoint {
  Random r = new Random();
  @GetMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
  public String hello(@RequestParam("name") String name) {
    try {
      if(r.nextBoolean()) {
        Thread.sleep(1000);
      }
      
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return name;
  }
}
