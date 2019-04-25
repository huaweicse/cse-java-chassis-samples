package com.huawei.cse.houseapp.customer;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableServiceComb
@EnableConfigurationProperties
public class CustomerApplication {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(CustomerApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
