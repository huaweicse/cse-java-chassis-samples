package com.huawei.cse.houseapp.product;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;

@SpringBootApplication(exclude = DispatcherServletAutoConfiguration.class)
@EnableServiceComb
public class ProductApplication {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(ProductApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
