package com.huawei.cse.porter.user.endpoint;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@RestSchema(schemaId = "TimeoutEndpoint")
@RequestMapping(path = "/")
public class TimeoutEndpoint {
  RestTemplate client = RestTemplateBuilder.create();
  AtomicLong counter = new AtomicLong(0);
  @GetMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
  public String hello(@RequestParam("name") String name) {
    long c = counter.incrementAndGet();
    if(c % 10000 == 0) {
      System.out.println("************************ \n ************** \n *******get : " + c);
    }
    return client.getForObject("cse://user-service-a/hello?name=" + name, String.class);
  }
}
